import { Injectable } from '@angular/core';
import { BehaviorSubject, interval, Subscription } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class TimerService {
    private timerSubscription: Subscription | null = null;
    private remainingTimeSubject = new BehaviorSubject<number>(0);

    remainingTime$ = this.remainingTimeSubject.asObservable();

    startTimer(durationSeconds: number) {
        this.remainingTimeSubject.next(durationSeconds);
        this.timerSubscription = interval(1000).subscribe(() => {
            const current = this.remainingTimeSubject.value;
            if (current > 0) {
                this.remainingTimeSubject.next(current - 1);
            } else {
                this.stopTimer();
            }
        });
    }

    stopTimer() {
        if (this.timerSubscription) {
            this.timerSubscription.unsubscribe();
            this.timerSubscription = null;
        }
    }

    formatTime(seconds: number): string {
        const h = Math.floor(seconds / 3600);
        const m = Math.floor((seconds % 3600) / 60);
        const s = seconds % 60;
        return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
    }
}

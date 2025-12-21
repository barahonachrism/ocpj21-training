import { TestBed } from '@angular/core/testing';
import { TimerService } from './timer.service';
import { vi, beforeEach, afterEach, describe, it, expect } from 'vitest';

describe('TimerService', () => {
    let service: TimerService;

    beforeEach(() => {
        vi.useFakeTimers();
        TestBed.configureTestingModule({
            providers: [TimerService]
        });
        service = TestBed.inject(TimerService);
    });

    afterEach(() => {
        vi.restoreAllMocks();
        vi.useRealTimers();
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    it('should start timer and emit values', () => {
        let lastTime: number | undefined;
        service.remainingTime$.subscribe(time => lastTime = time);

        service.startTimer(120);
        vi.advanceTimersByTime(1000);
        expect(lastTime).toBe(119);

        service.stopTimer();
    });

    it('should format time correctly', () => {
        expect(service.formatTime(3661)).toBe('01:01:01');
        expect(service.formatTime(60)).toBe('00:01:00');
    });

    it('should stop timer', () => {
        let lastTime: number | undefined;
        service.remainingTime$.subscribe(time => lastTime = time);

        service.startTimer(10);
        vi.advanceTimersByTime(1000);
        expect(lastTime).toBe(9);

        service.stopTimer();
        vi.advanceTimersByTime(1000);
        expect(lastTime).toBe(9); // Should not change
    });
});

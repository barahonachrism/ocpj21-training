import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

/**
 * Component for the home page.
 * Displays the exam instructions and entry points.
 */
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent {

}

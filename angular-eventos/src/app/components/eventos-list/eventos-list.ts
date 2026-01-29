import { Component, OnInit, signal } from '@angular/core';
import { EventoService } from '../../services/evento.service';
import { Evento } from '../../models/evento';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-eventos-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './eventos-list.html',
  styleUrl: './eventos-list.scss',
})
export class EventosList implements OnInit {
  eventos = signal<Evento[]>([]);

  constructor(private eventoService: EventoService, private router: Router) {
  }

  ngOnInit(): void {
    this.eventoService.getEvents().subscribe((data) => {
      this.eventos.set(data);
    });
  }

  goToNewEvento() {
    this.router.navigate(['/eventos/nuevo']);
  }
}


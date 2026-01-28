import { Component, OnInit, signal } from '@angular/core';
import { EventoService } from '../../services/evento.service';
import { Evento } from '../../models/evento';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-eventos-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './eventos-list.html',
  styleUrl: './eventos-list.scss',
})
export class EventosList implements OnInit {
  eventos = signal<Evento[]>([]);

  constructor(private eventoService: EventoService) {
  }

  ngOnInit(): void {
    this.eventoService.getEvents().subscribe((data) => {
      this.eventos.set(data);
    });
  }
}


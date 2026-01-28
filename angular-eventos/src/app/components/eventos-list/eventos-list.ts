import { Component, OnInit } from '@angular/core';
import { EventoService } from '../../services/evento.service';
import { Evento } from '../../models/evento';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-eventos-list',
  templateUrl: './eventos-list.html',
  styleUrl: './eventos-list.scss',
  providers: [EventoService, CommonModule]
})
export class EventosList implements OnInit {
  eventos: Evento[] = [];

  constructor(private eventoService: EventoService) {}

  ngOnInit(): void {
    this.eventoService.getEvents().subscribe((data) => {
      this.eventos = data;
    });
  }
}
import { Component } from '@angular/core';
import { Evento } from '../../models/evento';
import { EventoService } from '../../services/evento.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-eventos-new',
  templateUrl: './eventos-new.html',
  imports: [FormsModule],
  styleUrl: './eventos-new.scss',
})
export class EventosNew {
  evento: Partial<Evento> = {};

  constructor(private eventoService: EventoService, private router: Router) {}

  onSubmit() {
    const evento: Evento = {
      id: 0,
      nombre: this.evento.nombre || '',
      descripcion: this.evento.descripcion || '',
      fechaEvento: this.evento.fechaEvento || '',
      horaEvento: this.evento.horaEvento || '',
      precioMinimo: this.evento.precioMinimo ?? 0,
      precioMaximo: this.evento.precioMaximo ?? 0,
      localidad: this.evento.localidad || '',
      genero: this.evento.genero || '',
      nombreRecinto: this.evento.nombreRecinto || ''
    };
    this.eventoService.createEvent(evento).subscribe({
      next: () => this.router.navigate(['/eventos']),
      error: err => alert('Error creando evento: ' + (err?.error?.message || err.message))
    });
  }
}

import { Component, OnInit, signal } from '@angular/core';
import { Evento } from '../../models/evento';
import { EventoService } from '../../services/evento.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-eventos-new',
  templateUrl: './eventos-new.html',
  imports: [CommonModule, FormsModule],
  styleUrl: './eventos-new.scss',
})
export class EventosNew implements OnInit {
  evento = signal<Partial<Evento>>({});
  isEditMode = false;
  eventId: number | null = null;

  constructor(
    private eventoService: EventoService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.eventId = +id;
      this.isEditMode = true;
      this.eventoService.getEventById(this.eventId).subscribe({
        next: (data) => {
          this.evento.set(data);
        },
        error: (err) => alert('Error cargando evento: ' + (err?.error?.message || err.message))
      });
    }
  }

  onSubmit() {
    const currentEvento = this.evento();
    const evento: Evento = {
      id: this.eventId || 0,
      nombre: currentEvento.nombre || '',
      descripcion: currentEvento.descripcion || '',
      fechaEvento: currentEvento.fechaEvento || '',
      horaEvento: currentEvento.horaEvento || '',
      precioMinimo: currentEvento.precioMinimo ?? 0,
      precioMaximo: currentEvento.precioMaximo ?? 0,
      localidad: currentEvento.localidad || '',
      genero: currentEvento.genero || '',
      nombreRecinto: currentEvento.nombreRecinto || ''
    };

    if (this.isEditMode && this.eventId) {
      this.eventoService.updateEvent(this.eventId, evento).subscribe({
        next: () => this.router.navigate(['/eventos']),
        error: err => alert('Error actualizando evento: ' + (err?.error?.message || err.message))
      });
    } else {
      this.eventoService.createEvent(evento).subscribe({
        next: () => this.router.navigate(['/eventos']),
        error: err => alert('Error creando evento: ' + (err?.error?.message || err.message))
      });
    }
  }
}

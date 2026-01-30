import { Evento } from '../models/evento';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class EventoService {
  constructor(private http: HttpClient) {}

  // Gateway Port being used, be sure to start it
  private eventsUrl = 'http://localhost:8081/eventos';

  getEvents(): Observable<Evento[]> {
    return this.http.get<Evento[]>(this.eventsUrl);
  }

  getEventById(id: number): Observable<Evento> {
    return this.http.get<Evento>(`${this.eventsUrl}/${id}`);
  }

  createEvent(evento: Evento): Observable<Evento> {
    return this.http.post<Evento>(this.eventsUrl, evento);
  }

  updateEvent(id: number, evento: Evento): Observable<Evento> {
    return this.http.put<Evento>(`${this.eventsUrl}/${id}`, evento);
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.eventsUrl}/${id}`);
  }

}

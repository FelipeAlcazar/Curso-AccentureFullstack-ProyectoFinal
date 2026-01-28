import { Evento } from '../models/evento';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class EventoService {
  constructor(private http: HttpClient) {}

  private eventsUrl = 'http://localhost:7777/eventos';

    getEvents(): Observable<Evento[]> {
    return this.http.get<Evento[]>(this.eventsUrl);
  }

}

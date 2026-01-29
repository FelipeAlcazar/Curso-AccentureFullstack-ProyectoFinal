import { Routes } from '@angular/router';
import { EventosList } from './components/eventos-list/eventos-list';
import { EventosNew } from './components/eventos-new/eventos-new';

export const routes: Routes = [
    { path: '', redirectTo: 'eventos', pathMatch: 'full' },
    { path: 'eventos', component: EventosList },
    { path: 'eventos/nuevo', component: EventosNew }
];

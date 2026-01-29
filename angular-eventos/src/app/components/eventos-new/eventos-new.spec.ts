import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventosNew } from './eventos-new';

describe('EventosNew', () => {
  let component: EventosNew;
  let fixture: ComponentFixture<EventosNew>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventosNew]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventosNew);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

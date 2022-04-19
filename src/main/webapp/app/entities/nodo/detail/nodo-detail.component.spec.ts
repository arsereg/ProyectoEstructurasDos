import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NodoDetailComponent } from './nodo-detail.component';

describe('Nodo Management Detail Component', () => {
  let comp: NodoDetailComponent;
  let fixture: ComponentFixture<NodoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NodoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ nodo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NodoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NodoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load nodo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.nodo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

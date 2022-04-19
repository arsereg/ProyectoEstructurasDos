import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NodoService } from '../service/nodo.service';
import { INodo, Nodo } from '../nodo.model';

import { NodoUpdateComponent } from './nodo-update.component';

describe('Nodo Management Update Component', () => {
  let comp: NodoUpdateComponent;
  let fixture: ComponentFixture<NodoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nodoService: NodoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NodoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(NodoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NodoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nodoService = TestBed.inject(NodoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const nodo: INodo = { id: 456 };

      activatedRoute.data = of({ nodo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(nodo));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Nodo>>();
      const nodo = { id: 123 };
      jest.spyOn(nodoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nodo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nodo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(nodoService.update).toHaveBeenCalledWith(nodo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Nodo>>();
      const nodo = new Nodo();
      jest.spyOn(nodoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nodo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nodo }));
      saveSubject.complete();

      // THEN
      expect(nodoService.create).toHaveBeenCalledWith(nodo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Nodo>>();
      const nodo = { id: 123 };
      jest.spyOn(nodoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nodo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nodoService.update).toHaveBeenCalledWith(nodo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

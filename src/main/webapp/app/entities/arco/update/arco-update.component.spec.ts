import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ArcoService } from '../service/arco.service';
import { IArco, Arco } from '../arco.model';
import { INodo } from 'app/entities/nodo/nodo.model';
import { NodoService } from 'app/entities/nodo/service/nodo.service';

import { ArcoUpdateComponent } from './arco-update.component';

describe('Arco Management Update Component', () => {
  let comp: ArcoUpdateComponent;
  let fixture: ComponentFixture<ArcoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let arcoService: ArcoService;
  let nodoService: NodoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ArcoUpdateComponent],
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
      .overrideTemplate(ArcoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArcoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    arcoService = TestBed.inject(ArcoService);
    nodoService = TestBed.inject(NodoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call from query and add missing value', () => {
      const arco: IArco = { id: 456 };
      const from: INodo = { id: 8334 };
      arco.from = from;

      const fromCollection: INodo[] = [{ id: 8718 }];
      jest.spyOn(nodoService, 'query').mockReturnValue(of(new HttpResponse({ body: fromCollection })));
      const expectedCollection: INodo[] = [from, ...fromCollection];
      jest.spyOn(nodoService, 'addNodoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ arco });
      comp.ngOnInit();

      expect(nodoService.query).toHaveBeenCalled();
      expect(nodoService.addNodoToCollectionIfMissing).toHaveBeenCalledWith(fromCollection, from);
      expect(comp.fromsCollection).toEqual(expectedCollection);
    });

    it('Should call to query and add missing value', () => {
      const arco: IArco = { id: 456 };
      const to: INodo = { id: 4515 };
      arco.to = to;

      const toCollection: INodo[] = [{ id: 56056 }];
      jest.spyOn(nodoService, 'query').mockReturnValue(of(new HttpResponse({ body: toCollection })));
      const expectedCollection: INodo[] = [to, ...toCollection];
      jest.spyOn(nodoService, 'addNodoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ arco });
      comp.ngOnInit();

      expect(nodoService.query).toHaveBeenCalled();
      expect(nodoService.addNodoToCollectionIfMissing).toHaveBeenCalledWith(toCollection, to);
      expect(comp.tosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const arco: IArco = { id: 456 };
      const from: INodo = { id: 15515 };
      arco.from = from;
      const to: INodo = { id: 64222 };
      arco.to = to;

      activatedRoute.data = of({ arco });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(arco));
      expect(comp.fromsCollection).toContain(from);
      expect(comp.tosCollection).toContain(to);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Arco>>();
      const arco = { id: 123 };
      jest.spyOn(arcoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arco }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(arcoService.update).toHaveBeenCalledWith(arco);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Arco>>();
      const arco = new Arco();
      jest.spyOn(arcoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arco }));
      saveSubject.complete();

      // THEN
      expect(arcoService.create).toHaveBeenCalledWith(arco);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Arco>>();
      const arco = { id: 123 };
      jest.spyOn(arcoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(arcoService.update).toHaveBeenCalledWith(arco);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackNodoById', () => {
      it('Should return tracked Nodo primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackNodoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

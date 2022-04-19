import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INodo, Nodo } from '../nodo.model';

import { NodoService } from './nodo.service';

describe('Nodo Service', () => {
  let service: NodoService;
  let httpMock: HttpTestingController;
  let elemDefault: INodo;
  let expectedResult: INodo | INodo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NodoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      x: 0,
      y: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Nodo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Nodo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Nodo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          x: 1,
          y: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Nodo', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          x: 1,
          y: 1,
        },
        new Nodo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Nodo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          x: 1,
          y: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Nodo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNodoToCollectionIfMissing', () => {
      it('should add a Nodo to an empty array', () => {
        const nodo: INodo = { id: 123 };
        expectedResult = service.addNodoToCollectionIfMissing([], nodo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nodo);
      });

      it('should not add a Nodo to an array that contains it', () => {
        const nodo: INodo = { id: 123 };
        const nodoCollection: INodo[] = [
          {
            ...nodo,
          },
          { id: 456 },
        ];
        expectedResult = service.addNodoToCollectionIfMissing(nodoCollection, nodo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Nodo to an array that doesn't contain it", () => {
        const nodo: INodo = { id: 123 };
        const nodoCollection: INodo[] = [{ id: 456 }];
        expectedResult = service.addNodoToCollectionIfMissing(nodoCollection, nodo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nodo);
      });

      it('should add only unique Nodo to an array', () => {
        const nodoArray: INodo[] = [{ id: 123 }, { id: 456 }, { id: 82490 }];
        const nodoCollection: INodo[] = [{ id: 123 }];
        expectedResult = service.addNodoToCollectionIfMissing(nodoCollection, ...nodoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nodo: INodo = { id: 123 };
        const nodo2: INodo = { id: 456 };
        expectedResult = service.addNodoToCollectionIfMissing([], nodo, nodo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nodo);
        expect(expectedResult).toContain(nodo2);
      });

      it('should accept null and undefined values', () => {
        const nodo: INodo = { id: 123 };
        expectedResult = service.addNodoToCollectionIfMissing([], null, nodo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nodo);
      });

      it('should return initial array if no Nodo is added', () => {
        const nodoCollection: INodo[] = [{ id: 123 }];
        expectedResult = service.addNodoToCollectionIfMissing(nodoCollection, undefined, null);
        expect(expectedResult).toEqual(nodoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

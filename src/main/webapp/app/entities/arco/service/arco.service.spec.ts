import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IArco, Arco } from '../arco.model';

import { ArcoService } from './arco.service';

describe('Arco Service', () => {
  let service: ArcoService;
  let httpMock: HttpTestingController;
  let elemDefault: IArco;
  let expectedResult: IArco | IArco[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ArcoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      weight: 0,
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

    it('should create a Arco', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Arco()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Arco', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          weight: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Arco', () => {
      const patchObject = Object.assign({}, new Arco());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Arco', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          weight: 1,
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

    it('should delete a Arco', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addArcoToCollectionIfMissing', () => {
      it('should add a Arco to an empty array', () => {
        const arco: IArco = { id: 123 };
        expectedResult = service.addArcoToCollectionIfMissing([], arco);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arco);
      });

      it('should not add a Arco to an array that contains it', () => {
        const arco: IArco = { id: 123 };
        const arcoCollection: IArco[] = [
          {
            ...arco,
          },
          { id: 456 },
        ];
        expectedResult = service.addArcoToCollectionIfMissing(arcoCollection, arco);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Arco to an array that doesn't contain it", () => {
        const arco: IArco = { id: 123 };
        const arcoCollection: IArco[] = [{ id: 456 }];
        expectedResult = service.addArcoToCollectionIfMissing(arcoCollection, arco);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arco);
      });

      it('should add only unique Arco to an array', () => {
        const arcoArray: IArco[] = [{ id: 123 }, { id: 456 }, { id: 99996 }];
        const arcoCollection: IArco[] = [{ id: 123 }];
        expectedResult = service.addArcoToCollectionIfMissing(arcoCollection, ...arcoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const arco: IArco = { id: 123 };
        const arco2: IArco = { id: 456 };
        expectedResult = service.addArcoToCollectionIfMissing([], arco, arco2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arco);
        expect(expectedResult).toContain(arco2);
      });

      it('should accept null and undefined values', () => {
        const arco: IArco = { id: 123 };
        expectedResult = service.addArcoToCollectionIfMissing([], null, arco, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arco);
      });

      it('should return initial array if no Arco is added', () => {
        const arcoCollection: IArco[] = [{ id: 123 }];
        expectedResult = service.addArcoToCollectionIfMissing(arcoCollection, undefined, null);
        expect(expectedResult).toEqual(arcoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INodo, getNodoIdentifier } from '../nodo.model';

export type EntityResponseType = HttpResponse<INodo>;
export type EntityArrayResponseType = HttpResponse<INodo[]>;

@Injectable({ providedIn: 'root' })
export class NodoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nodos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nodo: INodo): Observable<EntityResponseType> {
    return this.http.post<INodo>(this.resourceUrl, nodo, { observe: 'response' });
  }

  update(nodo: INodo): Observable<EntityResponseType> {
    return this.http.put<INodo>(`${this.resourceUrl}/${getNodoIdentifier(nodo) as number}`, nodo, { observe: 'response' });
  }

  partialUpdate(nodo: INodo): Observable<EntityResponseType> {
    return this.http.patch<INodo>(`${this.resourceUrl}/${getNodoIdentifier(nodo) as number}`, nodo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INodo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INodo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNodoToCollectionIfMissing(nodoCollection: INodo[], ...nodosToCheck: (INodo | null | undefined)[]): INodo[] {
    const nodos: INodo[] = nodosToCheck.filter(isPresent);
    if (nodos.length > 0) {
      const nodoCollectionIdentifiers = nodoCollection.map(nodoItem => getNodoIdentifier(nodoItem)!);
      const nodosToAdd = nodos.filter(nodoItem => {
        const nodoIdentifier = getNodoIdentifier(nodoItem);
        if (nodoIdentifier == null || nodoCollectionIdentifiers.includes(nodoIdentifier)) {
          return false;
        }
        nodoCollectionIdentifiers.push(nodoIdentifier);
        return true;
      });
      return [...nodosToAdd, ...nodoCollection];
    }
    return nodoCollection;
  }
}

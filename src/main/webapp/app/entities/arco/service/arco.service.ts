import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArco, getArcoIdentifier } from '../arco.model';

export type EntityResponseType = HttpResponse<IArco>;
export type EntityArrayResponseType = HttpResponse<IArco[]>;

@Injectable({ providedIn: 'root' })
export class ArcoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/arcos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(arco: IArco): Observable<EntityResponseType> {
    return this.http.post<IArco>(this.resourceUrl, arco, { observe: 'response' });
  }

  update(arco: IArco): Observable<EntityResponseType> {
    return this.http.put<IArco>(`${this.resourceUrl}/${getArcoIdentifier(arco) as number}`, arco, { observe: 'response' });
  }

  partialUpdate(arco: IArco): Observable<EntityResponseType> {
    return this.http.patch<IArco>(`${this.resourceUrl}/${getArcoIdentifier(arco) as number}`, arco, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArco>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArco[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addArcoToCollectionIfMissing(arcoCollection: IArco[], ...arcosToCheck: (IArco | null | undefined)[]): IArco[] {
    const arcos: IArco[] = arcosToCheck.filter(isPresent);
    if (arcos.length > 0) {
      const arcoCollectionIdentifiers = arcoCollection.map(arcoItem => getArcoIdentifier(arcoItem)!);
      const arcosToAdd = arcos.filter(arcoItem => {
        const arcoIdentifier = getArcoIdentifier(arcoItem);
        if (arcoIdentifier == null || arcoCollectionIdentifiers.includes(arcoIdentifier)) {
          return false;
        }
        arcoCollectionIdentifiers.push(arcoIdentifier);
        return true;
      });
      return [...arcosToAdd, ...arcoCollection];
    }
    return arcoCollection;
  }
}

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INodo, Nodo } from '../nodo.model';
import { NodoService } from '../service/nodo.service';

@Injectable({ providedIn: 'root' })
export class NodoRoutingResolveService implements Resolve<INodo> {
  constructor(protected service: NodoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INodo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nodo: HttpResponse<Nodo>) => {
          if (nodo.body) {
            return of(nodo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Nodo());
  }
}

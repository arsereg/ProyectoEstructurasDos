import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArco, Arco } from '../arco.model';
import { ArcoService } from '../service/arco.service';

@Injectable({ providedIn: 'root' })
export class ArcoRoutingResolveService implements Resolve<IArco> {
  constructor(protected service: ArcoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArco> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((arco: HttpResponse<Arco>) => {
          if (arco.body) {
            return of(arco.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Arco());
  }
}

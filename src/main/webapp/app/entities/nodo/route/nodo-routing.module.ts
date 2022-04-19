import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NodoComponent } from '../list/nodo.component';
import { NodoDetailComponent } from '../detail/nodo-detail.component';
import { NodoUpdateComponent } from '../update/nodo-update.component';
import { NodoRoutingResolveService } from './nodo-routing-resolve.service';

const nodoRoute: Routes = [
  {
    path: '',
    component: NodoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NodoDetailComponent,
    resolve: {
      nodo: NodoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NodoUpdateComponent,
    resolve: {
      nodo: NodoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NodoUpdateComponent,
    resolve: {
      nodo: NodoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nodoRoute)],
  exports: [RouterModule],
})
export class NodoRoutingModule {}

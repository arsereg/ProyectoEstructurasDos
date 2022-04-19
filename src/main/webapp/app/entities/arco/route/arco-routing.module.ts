import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArcoComponent } from '../list/arco.component';
import { ArcoDetailComponent } from '../detail/arco-detail.component';
import { ArcoUpdateComponent } from '../update/arco-update.component';
import { ArcoRoutingResolveService } from './arco-routing-resolve.service';

const arcoRoute: Routes = [
  {
    path: '',
    component: ArcoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArcoDetailComponent,
    resolve: {
      arco: ArcoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArcoUpdateComponent,
    resolve: {
      arco: ArcoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArcoUpdateComponent,
    resolve: {
      arco: ArcoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(arcoRoute)],
  exports: [RouterModule],
})
export class ArcoRoutingModule {}

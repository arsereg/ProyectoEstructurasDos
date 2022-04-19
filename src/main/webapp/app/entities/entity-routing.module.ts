import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'nodo',
        data: { pageTitle: 'proyectoEstructurasDosApp.nodo.home.title' },
        loadChildren: () => import('./nodo/nodo.module').then(m => m.NodoModule),
      },
      {
        path: 'arco',
        data: { pageTitle: 'proyectoEstructurasDosApp.arco.home.title' },
        loadChildren: () => import('./arco/arco.module').then(m => m.ArcoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

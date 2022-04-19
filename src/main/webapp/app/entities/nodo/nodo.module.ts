import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NodoComponent } from './list/nodo.component';
import { NodoDetailComponent } from './detail/nodo-detail.component';
import { NodoUpdateComponent } from './update/nodo-update.component';
import { NodoDeleteDialogComponent } from './delete/nodo-delete-dialog.component';
import { NodoRoutingModule } from './route/nodo-routing.module';

@NgModule({
  imports: [SharedModule, NodoRoutingModule],
  declarations: [NodoComponent, NodoDetailComponent, NodoUpdateComponent, NodoDeleteDialogComponent],
  entryComponents: [NodoDeleteDialogComponent],
})
export class NodoModule {}

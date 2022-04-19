import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ArcoComponent } from './list/arco.component';
import { ArcoDetailComponent } from './detail/arco-detail.component';
import { ArcoUpdateComponent } from './update/arco-update.component';
import { ArcoDeleteDialogComponent } from './delete/arco-delete-dialog.component';
import { ArcoRoutingModule } from './route/arco-routing.module';

@NgModule({
  imports: [SharedModule, ArcoRoutingModule],
  declarations: [ArcoComponent, ArcoDetailComponent, ArcoUpdateComponent, ArcoDeleteDialogComponent],
  entryComponents: [ArcoDeleteDialogComponent],
})
export class ArcoModule {}

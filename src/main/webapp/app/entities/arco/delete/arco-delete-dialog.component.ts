import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArco } from '../arco.model';
import { ArcoService } from '../service/arco.service';

@Component({
  templateUrl: './arco-delete-dialog.component.html',
})
export class ArcoDeleteDialogComponent {
  arco?: IArco;

  constructor(protected arcoService: ArcoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.arcoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

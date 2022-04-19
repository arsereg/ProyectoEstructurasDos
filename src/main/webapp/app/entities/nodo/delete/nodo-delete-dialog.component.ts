import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INodo } from '../nodo.model';
import { NodoService } from '../service/nodo.service';

@Component({
  templateUrl: './nodo-delete-dialog.component.html',
})
export class NodoDeleteDialogComponent {
  nodo?: INodo;

  constructor(protected nodoService: NodoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nodoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

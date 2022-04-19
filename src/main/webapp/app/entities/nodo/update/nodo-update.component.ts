import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { INodo, Nodo } from '../nodo.model';
import { NodoService } from '../service/nodo.service';

@Component({
  selector: 'jhi-nodo-update',
  templateUrl: './nodo-update.component.html',
})
export class NodoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    x: [],
    y: [],
  });

  constructor(protected nodoService: NodoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nodo }) => {
      this.updateForm(nodo);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nodo = this.createFromForm();
    if (nodo.id !== undefined) {
      this.subscribeToSaveResponse(this.nodoService.update(nodo));
    } else {
      this.subscribeToSaveResponse(this.nodoService.create(nodo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INodo>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(nodo: INodo): void {
    this.editForm.patchValue({
      id: nodo.id,
      name: nodo.name,
      x: nodo.x,
      y: nodo.y,
    });
  }

  protected createFromForm(): INodo {
    return {
      ...new Nodo(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      x: this.editForm.get(['x'])!.value,
      y: this.editForm.get(['y'])!.value,
    };
  }
}

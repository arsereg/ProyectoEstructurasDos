import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IArco, Arco } from '../arco.model';
import { ArcoService } from '../service/arco.service';
import { INodo } from 'app/entities/nodo/nodo.model';
import { NodoService } from 'app/entities/nodo/service/nodo.service';

@Component({
  selector: 'jhi-arco-update',
  templateUrl: './arco-update.component.html',
})
export class ArcoUpdateComponent implements OnInit {
  isSaving = false;

  nodosSharedCollection: INodo[] = [];

  editForm = this.fb.group({
    id: [],
    weight: [],
    froms: [],
    tos: [],
  });

  constructor(
    protected arcoService: ArcoService,
    protected nodoService: NodoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arco }) => {
      this.updateForm(arco);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const arco = this.createFromForm();
    if (arco.id !== undefined) {
      this.subscribeToSaveResponse(this.arcoService.update(arco));
    } else {
      this.subscribeToSaveResponse(this.arcoService.create(arco));
    }
  }

  trackNodoById(index: number, item: INodo): number {
    return item.id!;
  }

  getSelectedNodo(option: INodo, selectedVals?: INodo[]): INodo {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArco>>): void {
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

  protected updateForm(arco: IArco): void {
    this.editForm.patchValue({
      id: arco.id,
      weight: arco.weight,
      froms: arco.froms,
      tos: arco.tos,
    });

    this.nodosSharedCollection = this.nodoService.addNodoToCollectionIfMissing(
      this.nodosSharedCollection,
      ...(arco.froms ?? []),
      ...(arco.tos ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.nodoService
      .query()
      .pipe(map((res: HttpResponse<INodo[]>) => res.body ?? []))
      .pipe(
        map((nodos: INodo[]) =>
          this.nodoService.addNodoToCollectionIfMissing(
            nodos,
            ...(this.editForm.get('froms')!.value ?? []),
            ...(this.editForm.get('tos')!.value ?? [])
          )
        )
      )
      .subscribe((nodos: INodo[]) => (this.nodosSharedCollection = nodos));
  }

  protected createFromForm(): IArco {
    return {
      ...new Arco(),
      id: this.editForm.get(['id'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      froms: this.editForm.get(['froms'])!.value,
      tos: this.editForm.get(['tos'])!.value,
    };
  }
}

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

  fromsCollection: INodo[] = [];
  tosCollection: INodo[] = [];

  editForm = this.fb.group({
    id: [],
    weight: [],
    from: [],
    to: [],
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
      from: arco.from,
      to: arco.to,
    });

    this.fromsCollection = this.nodoService.addNodoToCollectionIfMissing(this.fromsCollection, arco.from);
    this.tosCollection = this.nodoService.addNodoToCollectionIfMissing(this.tosCollection, arco.to);
  }

  protected loadRelationshipsOptions(): void {
    this.nodoService
      .query({ filter: 'arco-is-null' })
      .pipe(map((res: HttpResponse<INodo[]>) => res.body ?? []))
      .pipe(map((nodos: INodo[]) => this.nodoService.addNodoToCollectionIfMissing(nodos, this.editForm.get('from')!.value)))
      .subscribe((nodos: INodo[]) => (this.fromsCollection = nodos));

    this.nodoService
      .query({ filter: 'arco-is-null' })
      .pipe(map((res: HttpResponse<INodo[]>) => res.body ?? []))
      .pipe(map((nodos: INodo[]) => this.nodoService.addNodoToCollectionIfMissing(nodos, this.editForm.get('to')!.value)))
      .subscribe((nodos: INodo[]) => (this.tosCollection = nodos));
  }

  protected createFromForm(): IArco {
    return {
      ...new Arco(),
      id: this.editForm.get(['id'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      from: this.editForm.get(['from'])!.value,
      to: this.editForm.get(['to'])!.value,
    };
  }
}

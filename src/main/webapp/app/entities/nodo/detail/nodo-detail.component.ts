import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INodo } from '../nodo.model';

@Component({
  selector: 'jhi-nodo-detail',
  templateUrl: './nodo-detail.component.html',
})
export class NodoDetailComponent implements OnInit {
  nodo: INodo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nodo }) => {
      this.nodo = nodo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArco } from '../arco.model';

@Component({
  selector: 'jhi-arco-detail',
  templateUrl: './arco-detail.component.html',
})
export class ArcoDetailComponent implements OnInit {
  arco: IArco | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arco }) => {
      this.arco = arco;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

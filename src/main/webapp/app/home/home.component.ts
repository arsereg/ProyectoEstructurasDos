import { Component, OnInit, OnDestroy, AfterViewInit, AfterContentInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { Map, marker, polygon, tileLayer } from 'leaflet';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
  public map: Map;
  account: Account | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  ngAfterViewInit(): void {
    this.map = new Map('map');
    this.initializeMap();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  initializeMap(): void {
    this.map.setView([9.748917, -83.753428], 10);

    tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
      attribution: 'Estructuras de datos 2',
      maxZoom: 20,
      id: 'mapbox/streets-v11',
      tileSize: 512,
      zoomOffset: -1,
      accessToken: 'pk.eyJ1Ijoiam9zZWRhbmllbGNyIiwiYSI6ImNsMmZpaGp6ajA0aHAzZnB2YTQ4OXprazQifQ.743TNZFVNrLwk5_3YR-Wcg',
    }).addTo(this.map);
  }

  agregarPunto(): void {
    marker([9.9328, -84.0569]).addTo(this.map).bindPopup('Mall san pedro').openPopup();

    marker([9.8352904, -83.9616615]).addTo(this.map).bindPopup('Casa JD').openPopup();

    const p = polygon([
      [9.9328, -84.0569],
      [9.8352904, -83.9616615],
    ]).addTo(this.map);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

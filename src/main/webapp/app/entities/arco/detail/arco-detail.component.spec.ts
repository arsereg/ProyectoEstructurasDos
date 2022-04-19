import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArcoDetailComponent } from './arco-detail.component';

describe('Arco Management Detail Component', () => {
  let comp: ArcoDetailComponent;
  let fixture: ComponentFixture<ArcoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArcoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ arco: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ArcoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ArcoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load arco on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.arco).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

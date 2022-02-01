import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HomeIsolationsDetailComponent } from './home-isolations-detail.component';

describe('HomeIsolations Management Detail Component', () => {
  let comp: HomeIsolationsDetailComponent;
  let fixture: ComponentFixture<HomeIsolationsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HomeIsolationsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ homeIsolations: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HomeIsolationsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HomeIsolationsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load homeIsolations on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.homeIsolations).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IsolationsDetailsDetailComponent } from './isolations-details-detail.component';

describe('IsolationsDetails Management Detail Component', () => {
  let comp: IsolationsDetailsDetailComponent;
  let fixture: ComponentFixture<IsolationsDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IsolationsDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ isolationsDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IsolationsDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IsolationsDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load isolationsDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.isolationsDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

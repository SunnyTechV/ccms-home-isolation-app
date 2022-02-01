import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssessmentAnswersDetailComponent } from './assessment-answers-detail.component';

describe('AssessmentAnswers Management Detail Component', () => {
  let comp: AssessmentAnswersDetailComponent;
  let fixture: ComponentFixture<AssessmentAnswersDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssessmentAnswersDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assessmentAnswers: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssessmentAnswersDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssessmentAnswersDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assessmentAnswers on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assessmentAnswers).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssessmentAnswersService } from '../service/assessment-answers.service';
import { IAssessmentAnswers, AssessmentAnswers } from '../assessment-answers.model';
import { IQuestions } from 'app/entities/questions/questions.model';
import { QuestionsService } from 'app/entities/questions/service/questions.service';
import { IAssessment } from 'app/entities/assessment/assessment.model';
import { AssessmentService } from 'app/entities/assessment/service/assessment.service';

import { AssessmentAnswersUpdateComponent } from './assessment-answers-update.component';

describe('AssessmentAnswers Management Update Component', () => {
  let comp: AssessmentAnswersUpdateComponent;
  let fixture: ComponentFixture<AssessmentAnswersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assessmentAnswersService: AssessmentAnswersService;
  let questionsService: QuestionsService;
  let assessmentService: AssessmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssessmentAnswersUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AssessmentAnswersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssessmentAnswersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assessmentAnswersService = TestBed.inject(AssessmentAnswersService);
    questionsService = TestBed.inject(QuestionsService);
    assessmentService = TestBed.inject(AssessmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call questions query and add missing value', () => {
      const assessmentAnswers: IAssessmentAnswers = { id: 456 };
      const questions: IQuestions = { id: 34767 };
      assessmentAnswers.questions = questions;

      const questionsCollection: IQuestions[] = [{ id: 45100 }];
      jest.spyOn(questionsService, 'query').mockReturnValue(of(new HttpResponse({ body: questionsCollection })));
      const expectedCollection: IQuestions[] = [questions, ...questionsCollection];
      jest.spyOn(questionsService, 'addQuestionsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assessmentAnswers });
      comp.ngOnInit();

      expect(questionsService.query).toHaveBeenCalled();
      expect(questionsService.addQuestionsToCollectionIfMissing).toHaveBeenCalledWith(questionsCollection, questions);
      expect(comp.questionsCollection).toEqual(expectedCollection);
    });

    it('Should call Assessment query and add missing value', () => {
      const assessmentAnswers: IAssessmentAnswers = { id: 456 };
      const assessment: IAssessment = { id: 60927 };
      assessmentAnswers.assessment = assessment;

      const assessmentCollection: IAssessment[] = [{ id: 85165 }];
      jest.spyOn(assessmentService, 'query').mockReturnValue(of(new HttpResponse({ body: assessmentCollection })));
      const additionalAssessments = [assessment];
      const expectedCollection: IAssessment[] = [...additionalAssessments, ...assessmentCollection];
      jest.spyOn(assessmentService, 'addAssessmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assessmentAnswers });
      comp.ngOnInit();

      expect(assessmentService.query).toHaveBeenCalled();
      expect(assessmentService.addAssessmentToCollectionIfMissing).toHaveBeenCalledWith(assessmentCollection, ...additionalAssessments);
      expect(comp.assessmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assessmentAnswers: IAssessmentAnswers = { id: 456 };
      const questions: IQuestions = { id: 14166 };
      assessmentAnswers.questions = questions;
      const assessment: IAssessment = { id: 51777 };
      assessmentAnswers.assessment = assessment;

      activatedRoute.data = of({ assessmentAnswers });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assessmentAnswers));
      expect(comp.questionsCollection).toContain(questions);
      expect(comp.assessmentsSharedCollection).toContain(assessment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssessmentAnswers>>();
      const assessmentAnswers = { id: 123 };
      jest.spyOn(assessmentAnswersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assessmentAnswers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assessmentAnswers }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assessmentAnswersService.update).toHaveBeenCalledWith(assessmentAnswers);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssessmentAnswers>>();
      const assessmentAnswers = new AssessmentAnswers();
      jest.spyOn(assessmentAnswersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assessmentAnswers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assessmentAnswers }));
      saveSubject.complete();

      // THEN
      expect(assessmentAnswersService.create).toHaveBeenCalledWith(assessmentAnswers);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssessmentAnswers>>();
      const assessmentAnswers = { id: 123 };
      jest.spyOn(assessmentAnswersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assessmentAnswers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assessmentAnswersService.update).toHaveBeenCalledWith(assessmentAnswers);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackQuestionsById', () => {
      it('Should return tracked Questions primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuestionsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAssessmentById', () => {
      it('Should return tracked Assessment primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssessmentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

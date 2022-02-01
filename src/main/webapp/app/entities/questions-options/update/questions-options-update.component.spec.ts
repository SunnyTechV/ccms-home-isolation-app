import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionsOptionsService } from '../service/questions-options.service';
import { IQuestionsOptions, QuestionsOptions } from '../questions-options.model';
import { IQuestions } from 'app/entities/questions/questions.model';
import { QuestionsService } from 'app/entities/questions/service/questions.service';

import { QuestionsOptionsUpdateComponent } from './questions-options-update.component';

describe('QuestionsOptions Management Update Component', () => {
  let comp: QuestionsOptionsUpdateComponent;
  let fixture: ComponentFixture<QuestionsOptionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionsOptionsService: QuestionsOptionsService;
  let questionsService: QuestionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionsOptionsUpdateComponent],
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
      .overrideTemplate(QuestionsOptionsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionsOptionsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionsOptionsService = TestBed.inject(QuestionsOptionsService);
    questionsService = TestBed.inject(QuestionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Questions query and add missing value', () => {
      const questionsOptions: IQuestionsOptions = { id: 456 };
      const questions: IQuestions = { id: 8877 };
      questionsOptions.questions = questions;

      const questionsCollection: IQuestions[] = [{ id: 15849 }];
      jest.spyOn(questionsService, 'query').mockReturnValue(of(new HttpResponse({ body: questionsCollection })));
      const additionalQuestions = [questions];
      const expectedCollection: IQuestions[] = [...additionalQuestions, ...questionsCollection];
      jest.spyOn(questionsService, 'addQuestionsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionsOptions });
      comp.ngOnInit();

      expect(questionsService.query).toHaveBeenCalled();
      expect(questionsService.addQuestionsToCollectionIfMissing).toHaveBeenCalledWith(questionsCollection, ...additionalQuestions);
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const questionsOptions: IQuestionsOptions = { id: 456 };
      const questions: IQuestions = { id: 23149 };
      questionsOptions.questions = questions;

      activatedRoute.data = of({ questionsOptions });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(questionsOptions));
      expect(comp.questionsSharedCollection).toContain(questions);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionsOptions>>();
      const questionsOptions = { id: 123 };
      jest.spyOn(questionsOptionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionsOptions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionsOptions }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionsOptionsService.update).toHaveBeenCalledWith(questionsOptions);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionsOptions>>();
      const questionsOptions = new QuestionsOptions();
      jest.spyOn(questionsOptionsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionsOptions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionsOptions }));
      saveSubject.complete();

      // THEN
      expect(questionsOptionsService.create).toHaveBeenCalledWith(questionsOptions);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<QuestionsOptions>>();
      const questionsOptions = { id: 123 };
      jest.spyOn(questionsOptionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionsOptions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionsOptionsService.update).toHaveBeenCalledWith(questionsOptions);
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
  });
});

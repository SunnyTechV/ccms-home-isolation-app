import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAssessmentAnswers, AssessmentAnswers } from '../assessment-answers.model';
import { AssessmentAnswersService } from '../service/assessment-answers.service';
import { IQuestions } from 'app/entities/questions/questions.model';
import { QuestionsService } from 'app/entities/questions/service/questions.service';
import { IAssessment } from 'app/entities/assessment/assessment.model';
import { AssessmentService } from 'app/entities/assessment/service/assessment.service';

@Component({
  selector: 'jhi-assessment-answers-update',
  templateUrl: './assessment-answers-update.component.html',
})
export class AssessmentAnswersUpdateComponent implements OnInit {
  isSaving = false;

  questionsCollection: IQuestions[] = [];
  assessmentsSharedCollection: IAssessment[] = [];

  editForm = this.fb.group({
    id: [],
    answer: [],
    lastModified: [],
    lastModifiedBy: [],
    questions: [],
    assessment: [],
  });

  constructor(
    protected assessmentAnswersService: AssessmentAnswersService,
    protected questionsService: QuestionsService,
    protected assessmentService: AssessmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assessmentAnswers }) => {
      if (assessmentAnswers.id === undefined) {
        const today = dayjs().startOf('day');
        assessmentAnswers.lastModified = today;
      }

      this.updateForm(assessmentAnswers);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assessmentAnswers = this.createFromForm();
    if (assessmentAnswers.id !== undefined) {
      this.subscribeToSaveResponse(this.assessmentAnswersService.update(assessmentAnswers));
    } else {
      this.subscribeToSaveResponse(this.assessmentAnswersService.create(assessmentAnswers));
    }
  }

  trackQuestionsById(index: number, item: IQuestions): number {
    return item.id!;
  }

  trackAssessmentById(index: number, item: IAssessment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssessmentAnswers>>): void {
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

  protected updateForm(assessmentAnswers: IAssessmentAnswers): void {
    this.editForm.patchValue({
      id: assessmentAnswers.id,
      answer: assessmentAnswers.answer,
      lastModified: assessmentAnswers.lastModified ? assessmentAnswers.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: assessmentAnswers.lastModifiedBy,
      questions: assessmentAnswers.questions,
      assessment: assessmentAnswers.assessment,
    });

    this.questionsCollection = this.questionsService.addQuestionsToCollectionIfMissing(
      this.questionsCollection,
      assessmentAnswers.questions
    );
    this.assessmentsSharedCollection = this.assessmentService.addAssessmentToCollectionIfMissing(
      this.assessmentsSharedCollection,
      assessmentAnswers.assessment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.questionsService
      .query({ 'assessmentAnswersId.specified': 'false' })
      .pipe(map((res: HttpResponse<IQuestions[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestions[]) =>
          this.questionsService.addQuestionsToCollectionIfMissing(questions, this.editForm.get('questions')!.value)
        )
      )
      .subscribe((questions: IQuestions[]) => (this.questionsCollection = questions));

    this.assessmentService
      .query()
      .pipe(map((res: HttpResponse<IAssessment[]>) => res.body ?? []))
      .pipe(
        map((assessments: IAssessment[]) =>
          this.assessmentService.addAssessmentToCollectionIfMissing(assessments, this.editForm.get('assessment')!.value)
        )
      )
      .subscribe((assessments: IAssessment[]) => (this.assessmentsSharedCollection = assessments));
  }

  protected createFromForm(): IAssessmentAnswers {
    return {
      ...new AssessmentAnswers(),
      id: this.editForm.get(['id'])!.value,
      answer: this.editForm.get(['answer'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      questions: this.editForm.get(['questions'])!.value,
      assessment: this.editForm.get(['assessment'])!.value,
    };
  }
}

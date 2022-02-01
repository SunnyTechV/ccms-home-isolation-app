import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IQuestions, Questions } from '../questions.model';
import { QuestionsService } from '../service/questions.service';
import { QuestionType } from 'app/entities/enumerations/question-type.model';

@Component({
  selector: 'jhi-questions-update',
  templateUrl: './questions-update.component.html',
})
export class QuestionsUpdateComponent implements OnInit {
  isSaving = false;
  questionTypeValues = Object.keys(QuestionType);

  editForm = this.fb.group({
    id: [],
    question: [],
    questionDesc: [],
    questionType: [],
    active: [],
    lastModified: [],
    lastModifiedBy: [],
  });

  constructor(protected questionsService: QuestionsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questions }) => {
      if (questions.id === undefined) {
        const today = dayjs().startOf('day');
        questions.lastModified = today;
      }

      this.updateForm(questions);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questions = this.createFromForm();
    if (questions.id !== undefined) {
      this.subscribeToSaveResponse(this.questionsService.update(questions));
    } else {
      this.subscribeToSaveResponse(this.questionsService.create(questions));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestions>>): void {
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

  protected updateForm(questions: IQuestions): void {
    this.editForm.patchValue({
      id: questions.id,
      question: questions.question,
      questionDesc: questions.questionDesc,
      questionType: questions.questionType,
      active: questions.active,
      lastModified: questions.lastModified ? questions.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: questions.lastModifiedBy,
    });
  }

  protected createFromForm(): IQuestions {
    return {
      ...new Questions(),
      id: this.editForm.get(['id'])!.value,
      question: this.editForm.get(['question'])!.value,
      questionDesc: this.editForm.get(['questionDesc'])!.value,
      questionType: this.editForm.get(['questionType'])!.value,
      active: this.editForm.get(['active'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
    };
  }
}

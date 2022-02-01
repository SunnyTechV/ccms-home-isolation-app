import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAssessment, Assessment } from '../assessment.model';
import { AssessmentService } from '../service/assessment.service';
import { IHomeIsolations } from 'app/entities/home-isolations/home-isolations.model';
import { HomeIsolationsService } from 'app/entities/home-isolations/service/home-isolations.service';

@Component({
  selector: 'jhi-assessment-update',
  templateUrl: './assessment-update.component.html',
})
export class AssessmentUpdateComponent implements OnInit {
  isSaving = false;

  homeIsolationsSharedCollection: IHomeIsolations[] = [];

  editForm = this.fb.group({
    id: [],
    assessmentDate: [null, [Validators.required]],
    lastModified: [],
    lastModifiedBy: [],
    homeIsolations: [],
  });

  constructor(
    protected assessmentService: AssessmentService,
    protected homeIsolationsService: HomeIsolationsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assessment }) => {
      if (assessment.id === undefined) {
        const today = dayjs().startOf('day');
        assessment.assessmentDate = today;
        assessment.lastModified = today;
      }

      this.updateForm(assessment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assessment = this.createFromForm();
    if (assessment.id !== undefined) {
      this.subscribeToSaveResponse(this.assessmentService.update(assessment));
    } else {
      this.subscribeToSaveResponse(this.assessmentService.create(assessment));
    }
  }

  trackHomeIsolationsById(index: number, item: IHomeIsolations): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssessment>>): void {
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

  protected updateForm(assessment: IAssessment): void {
    this.editForm.patchValue({
      id: assessment.id,
      assessmentDate: assessment.assessmentDate ? assessment.assessmentDate.format(DATE_TIME_FORMAT) : null,
      lastModified: assessment.lastModified ? assessment.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: assessment.lastModifiedBy,
      homeIsolations: assessment.homeIsolations,
    });

    this.homeIsolationsSharedCollection = this.homeIsolationsService.addHomeIsolationsToCollectionIfMissing(
      this.homeIsolationsSharedCollection,
      assessment.homeIsolations
    );
  }

  protected loadRelationshipsOptions(): void {
    this.homeIsolationsService
      .query()
      .pipe(map((res: HttpResponse<IHomeIsolations[]>) => res.body ?? []))
      .pipe(
        map((homeIsolations: IHomeIsolations[]) =>
          this.homeIsolationsService.addHomeIsolationsToCollectionIfMissing(homeIsolations, this.editForm.get('homeIsolations')!.value)
        )
      )
      .subscribe((homeIsolations: IHomeIsolations[]) => (this.homeIsolationsSharedCollection = homeIsolations));
  }

  protected createFromForm(): IAssessment {
    return {
      ...new Assessment(),
      id: this.editForm.get(['id'])!.value,
      assessmentDate: this.editForm.get(['assessmentDate'])!.value
        ? dayjs(this.editForm.get(['assessmentDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      homeIsolations: this.editForm.get(['homeIsolations'])!.value,
    };
  }
}

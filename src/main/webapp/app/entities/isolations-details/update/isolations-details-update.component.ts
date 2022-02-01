import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IIsolationsDetails, IsolationsDetails } from '../isolations-details.model';
import { IsolationsDetailsService } from '../service/isolations-details.service';

@Component({
  selector: 'jhi-isolations-details-update',
  templateUrl: './isolations-details-update.component.html',
})
export class IsolationsDetailsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    isolationStartDate: [],
    isolationEndDate: [],
    referredDrName: [],
    referredDrMobile: [],
    prescriptionUrl: [],
    reportUrl: [],
    remarks: [],
    selfRegistered: [],
    lastAssessment: [],
    lastModified: [],
    lastModifiedBy: [],
  });

  constructor(
    protected isolationsDetailsService: IsolationsDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isolationsDetails }) => {
      if (isolationsDetails.id === undefined) {
        const today = dayjs().startOf('day');
        isolationsDetails.isolationStartDate = today;
        isolationsDetails.isolationEndDate = today;
        isolationsDetails.lastAssessment = today;
        isolationsDetails.lastModified = today;
      }

      this.updateForm(isolationsDetails);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const isolationsDetails = this.createFromForm();
    if (isolationsDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.isolationsDetailsService.update(isolationsDetails));
    } else {
      this.subscribeToSaveResponse(this.isolationsDetailsService.create(isolationsDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIsolationsDetails>>): void {
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

  protected updateForm(isolationsDetails: IIsolationsDetails): void {
    this.editForm.patchValue({
      id: isolationsDetails.id,
      isolationStartDate: isolationsDetails.isolationStartDate ? isolationsDetails.isolationStartDate.format(DATE_TIME_FORMAT) : null,
      isolationEndDate: isolationsDetails.isolationEndDate ? isolationsDetails.isolationEndDate.format(DATE_TIME_FORMAT) : null,
      referredDrName: isolationsDetails.referredDrName,
      referredDrMobile: isolationsDetails.referredDrMobile,
      prescriptionUrl: isolationsDetails.prescriptionUrl,
      reportUrl: isolationsDetails.reportUrl,
      remarks: isolationsDetails.remarks,
      selfRegistered: isolationsDetails.selfRegistered,
      lastAssessment: isolationsDetails.lastAssessment ? isolationsDetails.lastAssessment.format(DATE_TIME_FORMAT) : null,
      lastModified: isolationsDetails.lastModified ? isolationsDetails.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: isolationsDetails.lastModifiedBy,
    });
  }

  protected createFromForm(): IIsolationsDetails {
    return {
      ...new IsolationsDetails(),
      id: this.editForm.get(['id'])!.value,
      isolationStartDate: this.editForm.get(['isolationStartDate'])!.value
        ? dayjs(this.editForm.get(['isolationStartDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      isolationEndDate: this.editForm.get(['isolationEndDate'])!.value
        ? dayjs(this.editForm.get(['isolationEndDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      referredDrName: this.editForm.get(['referredDrName'])!.value,
      referredDrMobile: this.editForm.get(['referredDrMobile'])!.value,
      prescriptionUrl: this.editForm.get(['prescriptionUrl'])!.value,
      reportUrl: this.editForm.get(['reportUrl'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      selfRegistered: this.editForm.get(['selfRegistered'])!.value,
      lastAssessment: this.editForm.get(['lastAssessment'])!.value
        ? dayjs(this.editForm.get(['lastAssessment'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
    };
  }
}

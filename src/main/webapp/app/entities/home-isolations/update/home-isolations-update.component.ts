import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IHomeIsolations, HomeIsolations } from '../home-isolations.model';
import { HomeIsolationsService } from '../service/home-isolations.service';
import { IIsolationsDetails } from 'app/entities/isolations-details/isolations-details.model';
import { IsolationsDetailsService } from 'app/entities/isolations-details/service/isolations-details.service';
import { IsolationStatus } from 'app/entities/enumerations/isolation-status.model';
import { HealthCondition } from 'app/entities/enumerations/health-condition.model';

@Component({
  selector: 'jhi-home-isolations-update',
  templateUrl: './home-isolations-update.component.html',
})
export class HomeIsolationsUpdateComponent implements OnInit {
  isSaving = false;
  isolationStatusValues = Object.keys(IsolationStatus);
  healthConditionValues = Object.keys(HealthCondition);

  isolationsDetailsCollection: IIsolationsDetails[] = [];

  editForm = this.fb.group({
    id: [],
    icmrId: [],
    firstName: [],
    lastName: [],
    latitude: [],
    longitude: [],
    email: [],
    imageUrl: [],
    activated: [],
    mobileNo: [null, [Validators.required]],
    passwordHash: [null, [Validators.required]],
    secondaryContactNo: [],
    aadharCardNo: [null, [Validators.required]],
    status: [],
    age: [],
    gender: [],
    stateId: [],
    districtId: [],
    talukaId: [],
    cityId: [],
    address: [],
    pincode: [],
    collectionDate: [],
    hospitalized: [],
    hospitalId: [],
    addressLatitude: [],
    addressLongitude: [],
    currentLatitude: [],
    currentLongitude: [],
    hospitalizationDate: [],
    healthCondition: [],
    remarks: [],
    ccmsUserId: [],
    selfRegistered: [],
    lastModified: [],
    lastModifiedBy: [],
    isolationsDetails: [],
  });

  constructor(
    protected homeIsolationsService: HomeIsolationsService,
    protected isolationsDetailsService: IsolationsDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ homeIsolations }) => {
      if (homeIsolations.id === undefined) {
        const today = dayjs().startOf('day');
        homeIsolations.collectionDate = today;
        homeIsolations.hospitalizationDate = today;
        homeIsolations.lastModified = today;
      }

      this.updateForm(homeIsolations);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const homeIsolations = this.createFromForm();
    if (homeIsolations.id !== undefined) {
      this.subscribeToSaveResponse(this.homeIsolationsService.update(homeIsolations));
    } else {
      this.subscribeToSaveResponse(this.homeIsolationsService.create(homeIsolations));
    }
  }

  trackIsolationsDetailsById(index: number, item: IIsolationsDetails): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHomeIsolations>>): void {
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

  protected updateForm(homeIsolations: IHomeIsolations): void {
    this.editForm.patchValue({
      id: homeIsolations.id,
      icmrId: homeIsolations.icmrId,
      firstName: homeIsolations.firstName,
      lastName: homeIsolations.lastName,
      latitude: homeIsolations.latitude,
      longitude: homeIsolations.longitude,
      email: homeIsolations.email,
      imageUrl: homeIsolations.imageUrl,
      activated: homeIsolations.activated,
      mobileNo: homeIsolations.mobileNo,
      passwordHash: homeIsolations.passwordHash,
      secondaryContactNo: homeIsolations.secondaryContactNo,
      aadharCardNo: homeIsolations.aadharCardNo,
      status: homeIsolations.status,
      age: homeIsolations.age,
      gender: homeIsolations.gender,
      stateId: homeIsolations.stateId,
      districtId: homeIsolations.districtId,
      talukaId: homeIsolations.talukaId,
      cityId: homeIsolations.cityId,
      address: homeIsolations.address,
      pincode: homeIsolations.pincode,
      collectionDate: homeIsolations.collectionDate ? homeIsolations.collectionDate.format(DATE_TIME_FORMAT) : null,
      hospitalized: homeIsolations.hospitalized,
      hospitalId: homeIsolations.hospitalId,
      addressLatitude: homeIsolations.addressLatitude,
      addressLongitude: homeIsolations.addressLongitude,
      currentLatitude: homeIsolations.currentLatitude,
      currentLongitude: homeIsolations.currentLongitude,
      hospitalizationDate: homeIsolations.hospitalizationDate ? homeIsolations.hospitalizationDate.format(DATE_TIME_FORMAT) : null,
      healthCondition: homeIsolations.healthCondition,
      remarks: homeIsolations.remarks,
      ccmsUserId: homeIsolations.ccmsUserId,
      selfRegistered: homeIsolations.selfRegistered,
      lastModified: homeIsolations.lastModified ? homeIsolations.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: homeIsolations.lastModifiedBy,
      isolationsDetails: homeIsolations.isolationsDetails,
    });

    this.isolationsDetailsCollection = this.isolationsDetailsService.addIsolationsDetailsToCollectionIfMissing(
      this.isolationsDetailsCollection,
      homeIsolations.isolationsDetails
    );
  }

  protected loadRelationshipsOptions(): void {
    this.isolationsDetailsService
      .query({ 'homeIsolationsId.specified': 'false' })
      .pipe(map((res: HttpResponse<IIsolationsDetails[]>) => res.body ?? []))
      .pipe(
        map((isolationsDetails: IIsolationsDetails[]) =>
          this.isolationsDetailsService.addIsolationsDetailsToCollectionIfMissing(
            isolationsDetails,
            this.editForm.get('isolationsDetails')!.value
          )
        )
      )
      .subscribe((isolationsDetails: IIsolationsDetails[]) => (this.isolationsDetailsCollection = isolationsDetails));
  }

  protected createFromForm(): IHomeIsolations {
    return {
      ...new HomeIsolations(),
      id: this.editForm.get(['id'])!.value,
      icmrId: this.editForm.get(['icmrId'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      email: this.editForm.get(['email'])!.value,
      imageUrl: this.editForm.get(['imageUrl'])!.value,
      activated: this.editForm.get(['activated'])!.value,
      mobileNo: this.editForm.get(['mobileNo'])!.value,
      passwordHash: this.editForm.get(['passwordHash'])!.value,
      secondaryContactNo: this.editForm.get(['secondaryContactNo'])!.value,
      aadharCardNo: this.editForm.get(['aadharCardNo'])!.value,
      status: this.editForm.get(['status'])!.value,
      age: this.editForm.get(['age'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      stateId: this.editForm.get(['stateId'])!.value,
      districtId: this.editForm.get(['districtId'])!.value,
      talukaId: this.editForm.get(['talukaId'])!.value,
      cityId: this.editForm.get(['cityId'])!.value,
      address: this.editForm.get(['address'])!.value,
      pincode: this.editForm.get(['pincode'])!.value,
      collectionDate: this.editForm.get(['collectionDate'])!.value
        ? dayjs(this.editForm.get(['collectionDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      hospitalized: this.editForm.get(['hospitalized'])!.value,
      hospitalId: this.editForm.get(['hospitalId'])!.value,
      addressLatitude: this.editForm.get(['addressLatitude'])!.value,
      addressLongitude: this.editForm.get(['addressLongitude'])!.value,
      currentLatitude: this.editForm.get(['currentLatitude'])!.value,
      currentLongitude: this.editForm.get(['currentLongitude'])!.value,
      hospitalizationDate: this.editForm.get(['hospitalizationDate'])!.value
        ? dayjs(this.editForm.get(['hospitalizationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      healthCondition: this.editForm.get(['healthCondition'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      ccmsUserId: this.editForm.get(['ccmsUserId'])!.value,
      selfRegistered: this.editForm.get(['selfRegistered'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      isolationsDetails: this.editForm.get(['isolationsDetails'])!.value,
    };
  }
}

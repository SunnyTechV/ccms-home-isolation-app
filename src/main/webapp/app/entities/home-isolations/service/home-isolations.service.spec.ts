import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IsolationStatus } from 'app/entities/enumerations/isolation-status.model';
import { HealthCondition } from 'app/entities/enumerations/health-condition.model';
import { IHomeIsolations, HomeIsolations } from '../home-isolations.model';

import { HomeIsolationsService } from './home-isolations.service';

describe('HomeIsolations Service', () => {
  let service: HomeIsolationsService;
  let httpMock: HttpTestingController;
  let elemDefault: IHomeIsolations;
  let expectedResult: IHomeIsolations | IHomeIsolations[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HomeIsolationsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      icmrId: 'AAAAAAA',
      firstName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      latitude: 'AAAAAAA',
      longitude: 'AAAAAAA',
      email: 'AAAAAAA',
      imageUrl: 'AAAAAAA',
      activated: false,
      mobileNo: 'AAAAAAA',
      passwordHash: 'AAAAAAA',
      secondaryContactNo: 'AAAAAAA',
      aadharCardNo: 'AAAAAAA',
      status: IsolationStatus.HOMEISOLATION,
      age: 'AAAAAAA',
      gender: 'AAAAAAA',
      stateId: 0,
      districtId: 0,
      talukaId: 0,
      cityId: 0,
      address: 'AAAAAAA',
      pincode: 'AAAAAAA',
      collectionDate: currentDate,
      hospitalized: false,
      hospitalId: 0,
      addressLatitude: 'AAAAAAA',
      addressLongitude: 'AAAAAAA',
      currentLatitude: 'AAAAAAA',
      currentLongitude: 'AAAAAAA',
      hospitalizationDate: currentDate,
      healthCondition: HealthCondition.STABLE,
      remarks: 'AAAAAAA',
      ccmsUserId: 0,
      selfRegistered: false,
      lastModified: currentDate,
      lastModifiedBy: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          collectionDate: currentDate.format(DATE_TIME_FORMAT),
          hospitalizationDate: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a HomeIsolations', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          collectionDate: currentDate.format(DATE_TIME_FORMAT),
          hospitalizationDate: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          collectionDate: currentDate,
          hospitalizationDate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new HomeIsolations()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HomeIsolations', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          icmrId: 'BBBBBB',
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          latitude: 'BBBBBB',
          longitude: 'BBBBBB',
          email: 'BBBBBB',
          imageUrl: 'BBBBBB',
          activated: true,
          mobileNo: 'BBBBBB',
          passwordHash: 'BBBBBB',
          secondaryContactNo: 'BBBBBB',
          aadharCardNo: 'BBBBBB',
          status: 'BBBBBB',
          age: 'BBBBBB',
          gender: 'BBBBBB',
          stateId: 1,
          districtId: 1,
          talukaId: 1,
          cityId: 1,
          address: 'BBBBBB',
          pincode: 'BBBBBB',
          collectionDate: currentDate.format(DATE_TIME_FORMAT),
          hospitalized: true,
          hospitalId: 1,
          addressLatitude: 'BBBBBB',
          addressLongitude: 'BBBBBB',
          currentLatitude: 'BBBBBB',
          currentLongitude: 'BBBBBB',
          hospitalizationDate: currentDate.format(DATE_TIME_FORMAT),
          healthCondition: 'BBBBBB',
          remarks: 'BBBBBB',
          ccmsUserId: 1,
          selfRegistered: true,
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          collectionDate: currentDate,
          hospitalizationDate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HomeIsolations', () => {
      const patchObject = Object.assign(
        {
          icmrId: 'BBBBBB',
          latitude: 'BBBBBB',
          imageUrl: 'BBBBBB',
          mobileNo: 'BBBBBB',
          secondaryContactNo: 'BBBBBB',
          aadharCardNo: 'BBBBBB',
          status: 'BBBBBB',
          age: 'BBBBBB',
          gender: 'BBBBBB',
          stateId: 1,
          collectionDate: currentDate.format(DATE_TIME_FORMAT),
          hospitalized: true,
          addressLatitude: 'BBBBBB',
          hospitalizationDate: currentDate.format(DATE_TIME_FORMAT),
          healthCondition: 'BBBBBB',
          selfRegistered: true,
          lastModifiedBy: 'BBBBBB',
        },
        new HomeIsolations()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          collectionDate: currentDate,
          hospitalizationDate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HomeIsolations', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          icmrId: 'BBBBBB',
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          latitude: 'BBBBBB',
          longitude: 'BBBBBB',
          email: 'BBBBBB',
          imageUrl: 'BBBBBB',
          activated: true,
          mobileNo: 'BBBBBB',
          passwordHash: 'BBBBBB',
          secondaryContactNo: 'BBBBBB',
          aadharCardNo: 'BBBBBB',
          status: 'BBBBBB',
          age: 'BBBBBB',
          gender: 'BBBBBB',
          stateId: 1,
          districtId: 1,
          talukaId: 1,
          cityId: 1,
          address: 'BBBBBB',
          pincode: 'BBBBBB',
          collectionDate: currentDate.format(DATE_TIME_FORMAT),
          hospitalized: true,
          hospitalId: 1,
          addressLatitude: 'BBBBBB',
          addressLongitude: 'BBBBBB',
          currentLatitude: 'BBBBBB',
          currentLongitude: 'BBBBBB',
          hospitalizationDate: currentDate.format(DATE_TIME_FORMAT),
          healthCondition: 'BBBBBB',
          remarks: 'BBBBBB',
          ccmsUserId: 1,
          selfRegistered: true,
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          collectionDate: currentDate,
          hospitalizationDate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a HomeIsolations', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHomeIsolationsToCollectionIfMissing', () => {
      it('should add a HomeIsolations to an empty array', () => {
        const homeIsolations: IHomeIsolations = { id: 123 };
        expectedResult = service.addHomeIsolationsToCollectionIfMissing([], homeIsolations);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(homeIsolations);
      });

      it('should not add a HomeIsolations to an array that contains it', () => {
        const homeIsolations: IHomeIsolations = { id: 123 };
        const homeIsolationsCollection: IHomeIsolations[] = [
          {
            ...homeIsolations,
          },
          { id: 456 },
        ];
        expectedResult = service.addHomeIsolationsToCollectionIfMissing(homeIsolationsCollection, homeIsolations);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HomeIsolations to an array that doesn't contain it", () => {
        const homeIsolations: IHomeIsolations = { id: 123 };
        const homeIsolationsCollection: IHomeIsolations[] = [{ id: 456 }];
        expectedResult = service.addHomeIsolationsToCollectionIfMissing(homeIsolationsCollection, homeIsolations);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(homeIsolations);
      });

      it('should add only unique HomeIsolations to an array', () => {
        const homeIsolationsArray: IHomeIsolations[] = [{ id: 123 }, { id: 456 }, { id: 99834 }];
        const homeIsolationsCollection: IHomeIsolations[] = [{ id: 123 }];
        expectedResult = service.addHomeIsolationsToCollectionIfMissing(homeIsolationsCollection, ...homeIsolationsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const homeIsolations: IHomeIsolations = { id: 123 };
        const homeIsolations2: IHomeIsolations = { id: 456 };
        expectedResult = service.addHomeIsolationsToCollectionIfMissing([], homeIsolations, homeIsolations2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(homeIsolations);
        expect(expectedResult).toContain(homeIsolations2);
      });

      it('should accept null and undefined values', () => {
        const homeIsolations: IHomeIsolations = { id: 123 };
        expectedResult = service.addHomeIsolationsToCollectionIfMissing([], null, homeIsolations, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(homeIsolations);
      });

      it('should return initial array if no HomeIsolations is added', () => {
        const homeIsolationsCollection: IHomeIsolations[] = [{ id: 123 }];
        expectedResult = service.addHomeIsolationsToCollectionIfMissing(homeIsolationsCollection, undefined, null);
        expect(expectedResult).toEqual(homeIsolationsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

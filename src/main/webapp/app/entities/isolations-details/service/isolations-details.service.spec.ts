import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIsolationsDetails, IsolationsDetails } from '../isolations-details.model';

import { IsolationsDetailsService } from './isolations-details.service';

describe('IsolationsDetails Service', () => {
  let service: IsolationsDetailsService;
  let httpMock: HttpTestingController;
  let elemDefault: IIsolationsDetails;
  let expectedResult: IIsolationsDetails | IIsolationsDetails[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IsolationsDetailsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      isolationStartDate: currentDate,
      isolationEndDate: currentDate,
      referredDrName: 'AAAAAAA',
      referredDrMobile: 'AAAAAAA',
      prescriptionUrl: 'AAAAAAA',
      reportUrl: 'AAAAAAA',
      remarks: 'AAAAAAA',
      selfRegistered: false,
      lastAssessment: currentDate,
      lastModified: currentDate,
      lastModifiedBy: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          isolationStartDate: currentDate.format(DATE_TIME_FORMAT),
          isolationEndDate: currentDate.format(DATE_TIME_FORMAT),
          lastAssessment: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a IsolationsDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          isolationStartDate: currentDate.format(DATE_TIME_FORMAT),
          isolationEndDate: currentDate.format(DATE_TIME_FORMAT),
          lastAssessment: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          isolationStartDate: currentDate,
          isolationEndDate: currentDate,
          lastAssessment: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new IsolationsDetails()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IsolationsDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          isolationStartDate: currentDate.format(DATE_TIME_FORMAT),
          isolationEndDate: currentDate.format(DATE_TIME_FORMAT),
          referredDrName: 'BBBBBB',
          referredDrMobile: 'BBBBBB',
          prescriptionUrl: 'BBBBBB',
          reportUrl: 'BBBBBB',
          remarks: 'BBBBBB',
          selfRegistered: true,
          lastAssessment: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          isolationStartDate: currentDate,
          isolationEndDate: currentDate,
          lastAssessment: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IsolationsDetails', () => {
      const patchObject = Object.assign(
        {
          isolationStartDate: currentDate.format(DATE_TIME_FORMAT),
          isolationEndDate: currentDate.format(DATE_TIME_FORMAT),
          prescriptionUrl: 'BBBBBB',
          lastModifiedBy: 'BBBBBB',
        },
        new IsolationsDetails()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          isolationStartDate: currentDate,
          isolationEndDate: currentDate,
          lastAssessment: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IsolationsDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          isolationStartDate: currentDate.format(DATE_TIME_FORMAT),
          isolationEndDate: currentDate.format(DATE_TIME_FORMAT),
          referredDrName: 'BBBBBB',
          referredDrMobile: 'BBBBBB',
          prescriptionUrl: 'BBBBBB',
          reportUrl: 'BBBBBB',
          remarks: 'BBBBBB',
          selfRegistered: true,
          lastAssessment: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          isolationStartDate: currentDate,
          isolationEndDate: currentDate,
          lastAssessment: currentDate,
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

    it('should delete a IsolationsDetails', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIsolationsDetailsToCollectionIfMissing', () => {
      it('should add a IsolationsDetails to an empty array', () => {
        const isolationsDetails: IIsolationsDetails = { id: 123 };
        expectedResult = service.addIsolationsDetailsToCollectionIfMissing([], isolationsDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isolationsDetails);
      });

      it('should not add a IsolationsDetails to an array that contains it', () => {
        const isolationsDetails: IIsolationsDetails = { id: 123 };
        const isolationsDetailsCollection: IIsolationsDetails[] = [
          {
            ...isolationsDetails,
          },
          { id: 456 },
        ];
        expectedResult = service.addIsolationsDetailsToCollectionIfMissing(isolationsDetailsCollection, isolationsDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IsolationsDetails to an array that doesn't contain it", () => {
        const isolationsDetails: IIsolationsDetails = { id: 123 };
        const isolationsDetailsCollection: IIsolationsDetails[] = [{ id: 456 }];
        expectedResult = service.addIsolationsDetailsToCollectionIfMissing(isolationsDetailsCollection, isolationsDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isolationsDetails);
      });

      it('should add only unique IsolationsDetails to an array', () => {
        const isolationsDetailsArray: IIsolationsDetails[] = [{ id: 123 }, { id: 456 }, { id: 70911 }];
        const isolationsDetailsCollection: IIsolationsDetails[] = [{ id: 123 }];
        expectedResult = service.addIsolationsDetailsToCollectionIfMissing(isolationsDetailsCollection, ...isolationsDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const isolationsDetails: IIsolationsDetails = { id: 123 };
        const isolationsDetails2: IIsolationsDetails = { id: 456 };
        expectedResult = service.addIsolationsDetailsToCollectionIfMissing([], isolationsDetails, isolationsDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isolationsDetails);
        expect(expectedResult).toContain(isolationsDetails2);
      });

      it('should accept null and undefined values', () => {
        const isolationsDetails: IIsolationsDetails = { id: 123 };
        expectedResult = service.addIsolationsDetailsToCollectionIfMissing([], null, isolationsDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isolationsDetails);
      });

      it('should return initial array if no IsolationsDetails is added', () => {
        const isolationsDetailsCollection: IIsolationsDetails[] = [{ id: 123 }];
        expectedResult = service.addIsolationsDetailsToCollectionIfMissing(isolationsDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(isolationsDetailsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

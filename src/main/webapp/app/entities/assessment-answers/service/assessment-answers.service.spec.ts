import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssessmentAnswers, AssessmentAnswers } from '../assessment-answers.model';

import { AssessmentAnswersService } from './assessment-answers.service';

describe('AssessmentAnswers Service', () => {
  let service: AssessmentAnswersService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssessmentAnswers;
  let expectedResult: IAssessmentAnswers | IAssessmentAnswers[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssessmentAnswersService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      answer: 'AAAAAAA',
      lastModified: currentDate,
      lastModifiedBy: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AssessmentAnswers', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new AssessmentAnswers()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssessmentAnswers', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          answer: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssessmentAnswers', () => {
      const patchObject = Object.assign({}, new AssessmentAnswers());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssessmentAnswers', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          answer: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
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

    it('should delete a AssessmentAnswers', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssessmentAnswersToCollectionIfMissing', () => {
      it('should add a AssessmentAnswers to an empty array', () => {
        const assessmentAnswers: IAssessmentAnswers = { id: 123 };
        expectedResult = service.addAssessmentAnswersToCollectionIfMissing([], assessmentAnswers);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assessmentAnswers);
      });

      it('should not add a AssessmentAnswers to an array that contains it', () => {
        const assessmentAnswers: IAssessmentAnswers = { id: 123 };
        const assessmentAnswersCollection: IAssessmentAnswers[] = [
          {
            ...assessmentAnswers,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssessmentAnswersToCollectionIfMissing(assessmentAnswersCollection, assessmentAnswers);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssessmentAnswers to an array that doesn't contain it", () => {
        const assessmentAnswers: IAssessmentAnswers = { id: 123 };
        const assessmentAnswersCollection: IAssessmentAnswers[] = [{ id: 456 }];
        expectedResult = service.addAssessmentAnswersToCollectionIfMissing(assessmentAnswersCollection, assessmentAnswers);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assessmentAnswers);
      });

      it('should add only unique AssessmentAnswers to an array', () => {
        const assessmentAnswersArray: IAssessmentAnswers[] = [{ id: 123 }, { id: 456 }, { id: 80888 }];
        const assessmentAnswersCollection: IAssessmentAnswers[] = [{ id: 123 }];
        expectedResult = service.addAssessmentAnswersToCollectionIfMissing(assessmentAnswersCollection, ...assessmentAnswersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assessmentAnswers: IAssessmentAnswers = { id: 123 };
        const assessmentAnswers2: IAssessmentAnswers = { id: 456 };
        expectedResult = service.addAssessmentAnswersToCollectionIfMissing([], assessmentAnswers, assessmentAnswers2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assessmentAnswers);
        expect(expectedResult).toContain(assessmentAnswers2);
      });

      it('should accept null and undefined values', () => {
        const assessmentAnswers: IAssessmentAnswers = { id: 123 };
        expectedResult = service.addAssessmentAnswersToCollectionIfMissing([], null, assessmentAnswers, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assessmentAnswers);
      });

      it('should return initial array if no AssessmentAnswers is added', () => {
        const assessmentAnswersCollection: IAssessmentAnswers[] = [{ id: 123 }];
        expectedResult = service.addAssessmentAnswersToCollectionIfMissing(assessmentAnswersCollection, undefined, null);
        expect(expectedResult).toEqual(assessmentAnswersCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

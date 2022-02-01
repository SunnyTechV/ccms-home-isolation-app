import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAssessmentAnswers, AssessmentAnswers } from '../assessment-answers.model';
import { AssessmentAnswersService } from '../service/assessment-answers.service';

import { AssessmentAnswersRoutingResolveService } from './assessment-answers-routing-resolve.service';

describe('AssessmentAnswers routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssessmentAnswersRoutingResolveService;
  let service: AssessmentAnswersService;
  let resultAssessmentAnswers: IAssessmentAnswers | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(AssessmentAnswersRoutingResolveService);
    service = TestBed.inject(AssessmentAnswersService);
    resultAssessmentAnswers = undefined;
  });

  describe('resolve', () => {
    it('should return IAssessmentAnswers returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssessmentAnswers = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssessmentAnswers).toEqual({ id: 123 });
    });

    it('should return new IAssessmentAnswers if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssessmentAnswers = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssessmentAnswers).toEqual(new AssessmentAnswers());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssessmentAnswers })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssessmentAnswers = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssessmentAnswers).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

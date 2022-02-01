import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IIsolationsDetails, IsolationsDetails } from '../isolations-details.model';
import { IsolationsDetailsService } from '../service/isolations-details.service';

import { IsolationsDetailsRoutingResolveService } from './isolations-details-routing-resolve.service';

describe('IsolationsDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: IsolationsDetailsRoutingResolveService;
  let service: IsolationsDetailsService;
  let resultIsolationsDetails: IIsolationsDetails | undefined;

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
    routingResolveService = TestBed.inject(IsolationsDetailsRoutingResolveService);
    service = TestBed.inject(IsolationsDetailsService);
    resultIsolationsDetails = undefined;
  });

  describe('resolve', () => {
    it('should return IIsolationsDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIsolationsDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIsolationsDetails).toEqual({ id: 123 });
    });

    it('should return new IIsolationsDetails if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIsolationsDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultIsolationsDetails).toEqual(new IsolationsDetails());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as IsolationsDetails })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIsolationsDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIsolationsDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

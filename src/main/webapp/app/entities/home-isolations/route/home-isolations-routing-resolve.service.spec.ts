import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IHomeIsolations, HomeIsolations } from '../home-isolations.model';
import { HomeIsolationsService } from '../service/home-isolations.service';

import { HomeIsolationsRoutingResolveService } from './home-isolations-routing-resolve.service';

describe('HomeIsolations routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: HomeIsolationsRoutingResolveService;
  let service: HomeIsolationsService;
  let resultHomeIsolations: IHomeIsolations | undefined;

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
    routingResolveService = TestBed.inject(HomeIsolationsRoutingResolveService);
    service = TestBed.inject(HomeIsolationsService);
    resultHomeIsolations = undefined;
  });

  describe('resolve', () => {
    it('should return IHomeIsolations returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHomeIsolations = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHomeIsolations).toEqual({ id: 123 });
    });

    it('should return new IHomeIsolations if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHomeIsolations = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultHomeIsolations).toEqual(new HomeIsolations());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as HomeIsolations })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHomeIsolations = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHomeIsolations).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

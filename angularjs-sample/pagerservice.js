"use strict";
myApp.service('PagerService', function() {
    this.GetPager = function(totalItems, currentPage, pageSize, blockSize) {
        // default to first page
        currentPage = currentPage || 1;

        // default page size is 10
        pageSize = pageSize || 10;
        blockSize =blockSize || 10;

        // calculate total pages
        var totalPages = Math.ceil(totalItems / pageSize);

        // default pagination
        var startPage = currentPage - ((currentPage-1)%blockSize);
        var endPage = Math.min(startPage + (blockSize-1), totalPages);

        // more flexible pagination
        var midBlockPos = blockSize / 2;

        if (totalPages <= blockSize) {
            // less than 10 total pages so show all
            startPage = 1;
            endPage = totalPages;
        } else {
            // more than 10 total pages so calculate start and end pages
            if (currentPage <= (midBlockPos+1)) {
                startPage = 1;
                endPage = blockSize;
            } else if (currentPage + (midBlockPos-1) >= totalPages) {
                startPage = totalPages - (blockSize-1);
                endPage = totalPages;
            } else {
                startPage = currentPage - midBlockPos;
                endPage = currentPage + (midBlockPos-1);
            }
        }

        // calculate start and end item indexes
        var startIndex = (currentPage - 1) * pageSize;
        var endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);

        // create an array of pages to ng-repeat in the pager control
        var pages = _.range(startPage, endPage + 1);

        // return object with all pager properties required by the view
        return {
            totalItems: totalItems,
            currentPage: currentPage,
            pageSize: pageSize,
            totalPages: totalPages,
            startPage: startPage,
            endPage: endPage,
            startIndex: startIndex,
            endIndex: endIndex,
            pages: pages
        };
    };
});

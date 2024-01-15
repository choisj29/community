

function queryString(page){
    var categoryId = $("#category").val();
    var searchCondition = $("#searchKey").val();
    var searchKeyword = $("#searchKeyword").val();
    console.log(page);
    console.log(categoryId);

    let queryParams = new Map([
        ['page', (page) ? page : 1],
        ['searchCondition', searchCondition],
        ['searchKeyword', searchKeyword],
    ]);

    for(let key of queryParams.keys()){
        if(queryParams.get(key)==""||queryParams.get(key)==undefined||queryParams.get(key)==null){
            queryParams.delete(key);
        }
    }
    var queryString = '?' + new URLSearchParams(queryParams).toString();
    return queryString;
}

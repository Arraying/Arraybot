// noinspection JSUnusedGlobalSymbols
function pick(array) {
    if(!Array.isArray(array)) {
        // noinspection JSUnresolvedVariable
        channel.message("Error @ pick(array): provided parameter is not an array");
        return;
    }
    if(array.length === 0) {
        // noinspection JSUnresolvedVariable
        channel.message("Error @ pick(array): provided parameter is an empty array");
        return
    }
    var toSend = array[Math.floor(Math.random() * array.length)];
    // noinspection JSUnresolvedVariable
    channel.message(toSend);
}
export function millisToMinutesAndSeconds(millis: number): string {
    var minutes: number = Math.floor(millis / 60_000);
    var seconds: number = +((millis % 60_000) / 1000).toFixed(0);

    return (
        (seconds == 60) ?
        (minutes + 1) + ":00" :
        minutes + ":" + ((seconds < 10 ? "0" : "") + seconds)
    );
}
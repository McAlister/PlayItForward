HorseRace = {};

(function app () {
    var scoreRange = 1000,
        maxRounds = 15,
        maxPoints = 45,
        OFFSET = 1,
        horserace = document.getElementById('horserace');

    function getPosition(waveNum, score, round) {
        var raceHeight = horserace.clientHeight,
            raceWidth = horserace.clientWidth,
            marginWidth = getHorseWidth(),
            scoreZone = raceWidth - marginWidth - 20,
            waveHeight = Math.floor(raceHeight/(getWaveCount()+OFFSET));
        return {
            left: round < 9 ? Math.floor(marginWidth + (score/scoreRange) * scoreZone)
                            : Math.floor(marginWidth + ((score-18)/(scoreRange-18)) * scoreZone),
            top: waveHeight*(waveNum+1)
        }
    }

    function updateTooltip (horse, horseInfo) {
        var tdata = horse.parentNode.getElementsByClassName('tdata')[0];
        tdata.innerHTML = horseInfo.name + "<br>Points: " + horseInfo.score;
    }

    function calculatePositions(round) {
        var i, j, wave, waves, children, pos, stick, horse, marks, markpos,
            horseWidth = getHorseWidth();
        waves = document.getElementsByClassName('wave');
        for (i = 0; i < waves.length; i++) {
            pos = getPosition(i, 0, round);
            wave = waves[i];
            children = Array.prototype.slice.call(wave.children);
            wave.style.top = pos.top + 'px';
            wave.style.backgroundPosition = 0-20*i + 'px';
            stick = wave.getElementsByClassName('stick')[0];
            stick.style.left = (stick.style.left || Math.floor(horseWidth/2)) + 'px';
            horse = wave.getElementsByClassName('horse')[0];
            horse.style.width = horse.style.height = horseWidth + 'px';
            horse.style.left = (horse.style.left || 0) + 'px';

            marks = children.filter(function(el){return el.className === 'scoremark'});
            for (j = 0; j < marks.length; j++) {
                markpos = getPosition(i, parseInt(marks[j].textContent, 10), round);
                marks[j].style.top = Math.floor(horseWidth/2) + 'px';
                marks[j].style.left = markpos.left + 'px';
            }
        }
    }
    function addWave (horseInfo) {
        var wave, scoremark, mark, stick, horse, tooltip;

        wave = document.createElement('div');
        wave.className = 'wave';
        wave.id = 'wave_' + horseInfo.key;
        wave.style.width = '400%';
        wave.style.height = '300px';
        wave.style.position = "absolute";
        wave.style.left = '0px';
        wave.style.backgroundImage = 'url("sine.png")';
        wave.style.backgroundSize = '100px 300px';
        wave.style.backgroundRepeat = 'repeat-x';
        wave.ontouchstart = function() {this.className = 'wave touching'};
        wave.ontouchend = function() {this.className = 'wave'};

        for (scoremark = 0; scoremark <= maxPoints; scoremark += 3) {
            mark = document.createElement('span');
            mark.className = 'scoremark';
            mark.style.position = "absolute";
            mark.innerHTML = scoremark;
            wave.appendChild(mark);
        }

        stick = document.createElement('img');
        stick.className = 'stick';
        stick.style.position = 'absolute';
        stick.style.top = '20px';
        stick.style.width = '3px';
        stick.style.height = '500px';
        stick.src = "stick.png";
        wave.appendChild(stick);

        horse = document.createElement('img');
        horse.className = 'horse';
        horse.style.position = 'absolute';
        horse.style.top = '0px';
        if (horseInfo.img && !horseInfo.img.match(/\//)) {
            horseInfo.img = '/assets/race/oval/' + horseInfo.img;
        }
        horse.src = horseInfo.img || "placeholder.png";
        horse.onclick = function() {
            var waves;
            if (this.parentNode.classList.contains('clicked'))
                return this.parentNode.classList.remove('clicked');
            waves = Array.prototype.slice.call(document.getElementsByClassName('wave'));
            waves.forEach(function(el){el.classList.remove('clicked');});
            this.parentNode.classList.add('clicked');
        };
        wave.appendChild(horse);

        tooltip = document.createElement('div');
        tooltip.className = 'tooltip';
        tooltip.style.position = 'absolute';
        tooltip.style.top = '0px';
        tooltip.style.left = '100px';
        tooltip.style.zIndex = 1;
        tooltip.innerHTML = '<div class="profile"><img src="'+horse.src.replace('/oval/', '/')+'"></div>' +
            '<span class="tdata"></span>';
        wave.appendChild(tooltip);

        horserace.appendChild(wave);
        updateTooltip(horse, horseInfo);
    }
    function removeWave (key, round) {
        var wave = document.getElementById('wave_' + key);
        wave.parentNode.removeChild(wave);
        calculatePositions(round);
    }

    function getWaveCount() {
        return document.getElementsByClassName('wave').length;
    }
    function getHorseWidth() {
        return Math.max(1.2*Math.floor(horserace.clientHeight/(getWaveCount()+OFFSET)), 50);
    }

    function updateHorseScore (horse, horseInfo, round) {
        var pos = getPosition(0, horseInfo.score, round),
            stick = horse.parentNode.getElementsByClassName('stick')[0],
            tooltip = horse.parentNode.getElementsByClassName('tooltip')[0],
            horseWidth = getHorseWidth();
        stick.style.left = pos.left + 'px';
        horse.style.left = (pos.left-Math.floor(horseWidth/2)) + 'px';
        if (pos.left < horserace.clientWidth/2) {
            tooltip.style.left = (pos.left + horseWidth/2 + 10) + 'px';
        } else {
            tooltip.style.left = (pos.left - horseWidth/2 - 10 - tooltip.clientWidth) + 'px';
        }
        updateTooltip(horse, horseInfo);
    }

    function processScores(scorelist, round) {
        var maxScore = 0,
            waveKeys,
            roundDisplay = document.getElementById('horseround');

        scorelist.forEach(function (el) {
            maxScore = el.score > maxScore ? el.score : maxScore;
        });
        scoreRange = Math.min(maxScore-(maxScore%18)+18, maxPoints);

        if (!roundDisplay) {
            roundDisplay = document.createElement('span');
            roundDisplay.id = 'horseround';
            horserace.appendChild(roundDisplay);
        }
        if (round > 9) {
            roundDisplay.innerHTML = 'Day 2 Round ' + round;
        } else {
            roundDisplay.innerHTML = 'Day 1 Round ' + round;
        }

        waveKeys = Array.prototype.map.call(document.getElementsByClassName('wave'),
                function (w) { return w.id.replace('wave_', ''); });
        waveKeys.forEach(function (key) {
            var i;
            for (i = 0; i < scorelist.length; i++) {
                if (scorelist[i].key == key)
                    return;
            }
            removeWave(key, round);
        });
        scorelist.forEach(function (el) {
            if (!document.getElementById('wave_' + el.key)) {
                addWave(el);
            }
        });
        scorelist.forEach(function (el) {
            var wave = document.getElementById('wave_' + el.key);
            updateHorseScore(wave.getElementsByClassName('horse')[0], el, round);
        });
        calculatePositions(round);
    }

    function ajax(url, success) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url);
        xhr.onreadystatechange = function() {
            if (xhr.readyState>3 && xhr.status==200) {
                var json = JSON.parse(this.responseText);
                success(json);
            }
        };
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
        xhr.send();
        return xhr;
    }

    function getRound(event, round) {
        if (HorseRace.inflight)
            HorseRace.inflight.abort();
        HorseRace.inflightRound = round;
        HorseRace.inflight = ajax('/api/EventStanding/event/'+event+'/round/'+round, function(json) {
            processScores(json, round);
            HorseRace.inflightRound = undefined;
            HorseRace.inflight = undefined;
        });
    }

    HorseRace.replay = function (event, rounds) {
        var myTimer,
            round = 1,
            speed = window.location.search.match(/[?&]fast/) ? 300 : 1000;
        rounds = rounds || maxRounds;

        getRound(event, round);
        myTimer = window.setInterval(function roundTimer() {
            round++;
            getRound(event, round);
            if (round === rounds) {
                window.clearInterval(myTimer);
            }
        }, speed);

        // watch for esc and arrow keys
        document.addEventListener('keydown', function(e) {
            var key = window.event ? e.keyCode : e.which,
                myRound = HorseRace.inflightRound || round,
                stop = function() {
                    window.clearInterval(myTimer);
                    if (HorseRace.inflight)
                        HorseRace.inflight.abort();
                };

            if (key === 27) { // ESC
                stop();
            }
            if (key === 37 || key === 39) { // left arrow, right arrow
                stop();
                round = (key === 37 ? myRound-1 : myRound+1);
                round = round < 1 ? 1 : round > maxRounds ? maxRounds : round;
                getRound(event, round);
                document.getElementById('horseround').innerHTML = 'Loading round ' + round + ' ...';
            }
        });
    }
}());

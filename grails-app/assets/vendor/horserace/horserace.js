HorseRace = {};

(function app () {
    var scoreRange = 1000,
        maxPoints = 45,
        OFFSET = 1,
        TROT_STEPS = 4,
        horserace = document.getElementById('horserace'),
        roundDisplay = document.getElementById('horseround');

    function getPosition(waveNum, score, round) {
        var raceHeight = horserace.clientHeight,
            raceWidth = horserace.clientWidth,
            marginWidth = 50,
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
        tdata.innerHTML = horseInfo.name + "<br>Points: " + horseInfo.score + "<br>Rank: " + horseInfo.rank;
    }

    function moveHorsegroup(horsegroup, new_left, horseWidth) {
        var a = horsegroup._positions,
            start_left = (a && a[a.length-1]) || parseInt(horsegroup.style.left, 10) || Math.floor(horseWidth),
            diff = new_left - start_left,
            i, step, a;
        if (horsegroup.func_id) {
            window.clearInterval(horsegroup.func_id);
        }
        step = diff / TROT_STEPS;

        a = horsegroup._positions = [];
        if (TROT_STEPS > 1) {
            a.push(a.length ? a[a.length-1] : start_left);
        }
        for (i = 1; i < TROT_STEPS; i++) {
            a.push(Math.floor(start_left + i*step + Math.random()*0.4*step - 0.2*step));
        }
        a.push(new_left);

        horsegroup.func_id = window.setInterval(function() {
            if (!horsegroup._positions.length) {
                window.clearInterval(horsegroup.func_id);
            }
            horsegroup.style.left = horsegroup._positions.shift() + 'px';
        }, 180);
    };

    function calculatePositions(round) {
        var i, j, wave, waves, children, pos, horse, marks, markpos, horse, horsegroup,
            horseWidth = getHorseWidth();
        waves = horserace.getElementsByClassName('wave');
        for (i = 0; i < waves.length; i++) {
            pos = getPosition(i, 0, round);
            wave = waves[i];
            children = Array.prototype.slice.call(wave.children);
            wave.style.top = pos.top + 'px';
            wave.style.backgroundPosition = 0-20*i + 'px';
            horsegroup = wave.getElementsByClassName('horsegroup')[0];
            horsegroup.style.left = (horsegroup.style.left || Math.floor(horseWidth)) + 'px';
            horse = wave.getElementsByClassName('horse')[0];
            horse.style.width = horse.style.height = horseWidth + 'px';
            horse.style.left = (-1 * Math.floor(horseWidth/2)) + 'px';

            marks = children.filter(function(el){return el.className === 'scoremark'});
            for (j = 0; j < marks.length; j++) {
                markpos = getPosition(i, parseInt(marks[j].textContent, 10), round);
                marks[j].style.top = Math.floor(horseWidth/2) + 'px';
                marks[j].style.left = markpos.left + 'px';
            }
        }
    }
    function addWave (horseInfo, insertAfter) {
        var wave, scoremark, mark, horse, horsegroup, profileImg;

        wave = document.getElementById("waveTemplate").cloneNode(true);
        wave.id = 'wave_' + horseInfo.key;
        for (scoremark = 0; scoremark <= maxPoints; scoremark += 3) {
            mark = document.createElement('span');
            mark.className = 'scoremark';
            mark.innerHTML = scoremark;
            wave.appendChild(mark);
        }
        horsegroup = wave.getElementsByClassName("horsegroup")[0];

        horse = wave.getElementsByClassName("horse")[0];
        if (horseInfo.img && !horseInfo.img.match(/\//)) {
            horseInfo.img = 'https://s3-us-west-2.amazonaws.com/playitforward-magic/images/race/ovals/' + horseInfo.img;
        }
        horse.src = horseInfo.img || "placeholder.png";
        horse.onclick = function(e) {
            var wave = this.parentNode.parentNode;
            if (wave.classList.contains('clicked'))
                return wave.classList.remove('clicked');
            HorseRace.clearSelection();
            wave.classList.add('clicked');
            e.stopPropagation();
        };

        profileImg = wave.getElementsByClassName("profile")[0].getElementsByTagName("img")[0];
        profileImg.src = horse.src.replace('/ovals/', '/');

        if (insertAfter)
            horserace.insertBefore(wave, insertAfter.nextSibling);
        else
            horserace.insertBefore(wave, horserace.firstChild);
        updateTooltip(horse, horseInfo);
        return wave;
    }
    function removeWave (key, round) {
        var wave = document.getElementById('wave_' + key);
        wave.parentNode.removeChild(wave);
        calculatePositions(round);
    }

    function getWaveCount() {
        return horserace.getElementsByClassName('wave').length;
    }
    function getHorseWidth() {
        return Math.max(1.2*Math.floor(horserace.clientHeight/(getWaveCount()+OFFSET)), 50);
    }

    function updateHorseScore (horse, horseInfo, round) {
        var pos = getPosition(0, (round === 0 ? 0 : horseInfo.score), round),
            horsegroup = horse.parentNode,
            stick = horse.parentNode.getElementsByClassName('stick')[0],
            tooltip = horse.parentNode.getElementsByClassName('tooltip')[0],
            horseWidth = getHorseWidth();
        moveHorsegroup(horsegroup, pos.left, horseWidth);
        if (horseInfo.leader)
            horsegroup.classList.add('leader');
        else
            horsegroup.classList.remove('leader');
        if (horserace.clientWidth <= 480 ){
            tooltip.style.left = horserace.clientWidth/2-tooltip.offsetWidth/2 + 'px';
        } else {
            if (pos.left < horserace.clientWidth/2) {
                tooltip.style.left = (horseWidth/2 + 10) + 'px';
            } else {
                tooltip.style.left = (-1 * horseWidth/2 - 10 - tooltip.clientWidth) + 'px';
            }
        }
        updateTooltip(horse, horseInfo);
    }

    function processScores(scorelist, round) {
        var maxScore = 0,
            waveKeys, lastWave, lowestRank;

        scorelist.forEach(function (el) {
            maxScore = el.score > maxScore ? el.score : maxScore;
        });
        scoreRange = Math.min(maxScore-(maxScore%18)+18, maxPoints);

        if (scorelist.length === 0) {
            roundDisplay.innerHTML = 'No standings data yet';
        } else if (round > 9) {
            roundDisplay.innerHTML = 'Day 2 Round ' + round;
        } else {
            roundDisplay.innerHTML = 'Day 1 Round ' + round;
        }

        // add/remove waves as necessary, preserving order
        waveKeys = Array.prototype.map.call(horserace.getElementsByClassName('wave'),
                function (w) { return Number(w.id.replace('wave_', '')); });
        waveKeys.forEach(function removeIfNotListed (key) {
            var i;
            for (i = 0; i < scorelist.length; i++) {
                if (scorelist[i].key === key)
                    return;
            }
            removeWave(key, round);
        });
        scorelist.forEach(function (el) {
            var w = document.getElementById('wave_' + el.key);
            if (!w) {
                lastWave = addWave(el, lastWave);
            } else {
                lastWave = w;
            }
        });

        // find the leader
        lowestRank = 999999;
        scorelist.forEach(function (el) {
            lowestRank = Math.min(el.rank, lowestRank);
        });
        scorelist.forEach(function (el) {
            if (el.rank === lowestRank)
                el.leader = 1;
        });

        // update the horses
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
            if (xhr.readyState>3 && xhr.status===200) {
                var json = JSON.parse(this.responseText);
                success(json);
            }
        };
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
        xhr.send();
        return xhr;
    }

    function getLastRound(event, callback) {
        ajax('/api/EventStanding/event/'+event, function (json) {
            callback(json.lastRound);
        });
    }
    function getRound(event, round) {
        if (HorseRace.inflight)
            HorseRace.inflight.abort();
        HorseRace.inflightRound = round;
        HorseRace.inflight = ajax('/api/EventStanding/event/'+event+'/round/'+(round||1), function(json) {
            processScores(json, round);
            HorseRace.inflightRound = undefined;
            HorseRace.inflight = undefined;
        });
    }

    HorseRace.showRoundZero = function(event) {
        getRound(event, 0);
    };

    function replay(event, maxRounds) {
        var myTimer,
            fresh = window.location.search.match(/[?&]fresh/) ? 1 : 0,
            round = (!fresh && maxRounds > 9) ? 9 : 0,
            speed = window.location.search.match(/[?&]fast/) ? 300 : 1000,
            togglePlayPause = function(play) {
                var c = horserace.getElementsByClassName('controls')[0];
                c.getElementsByClassName( 'play')[0].style.display = play ? 'none' : 'inline';
                c.getElementsByClassName('pause')[0].style.display = play ? 'inline' : 'none';
            },
            state = 'play',
            konami = [38, 38, 40, 40, 37, 39, 37, 39, 65, 66, 0],
            konamiIndex = 0;

        // check every 30 seconds for a new round and play if appropriate
        window.setInterval(function updateLastRound() {
            getLastRound(event, function(newLastRound) {
                var origMaxRounds = maxRounds+0;
                if (newLastRound > maxRounds) {
                    maxRounds = newLastRound;
                    if (round === origMaxRounds && state === 'paused') {
                        HorseRace.play();
                    }
                }
            });
        }, 30000);

        HorseRace.pause = function () {
            state = 'paused';
            togglePlayPause(false);
            window.clearInterval(myTimer);
            if (HorseRace.inflight)
                HorseRace.inflight.abort();
        };
        HorseRace.play = function () {
            state = 'playing';
            togglePlayPause(true);
            round = round >= maxRounds ? round : round+1;
            getRound(event, round);
            myTimer = window.setInterval(function roundTimer() {
                if (round >= maxRounds) {
                    HorseRace.pause();
                }
                round = round >= maxRounds ? round : round+1;
                getRound(event, round);
            }, speed);
        };
        HorseRace.back = function () {
            HorseRace.pause();
            round = round <= 0 ? 0 : round-1;
            getRound(event, round);
            roundDisplay.innerHTML = 'Loading round ' + round + ' ...';
        };
        HorseRace.next = function () {
            HorseRace.pause();
            round = round >= maxRounds ? maxRounds : round+1;
            getRound(event, round);
            roundDisplay.innerHTML = 'Loading round ' + round + ' ...';
        };

        HorseRace.play();

        // watch for esc and arrow keys
        document.addEventListener('keydown', function(e) {
            var key = window.event ? e.keyCode : e.which;
            if (key === konami[konamiIndex]) {
                konamiIndex++;
                if (konami[konamiIndex] === 0) {
                    HorseRace.pause();
                    HorseRace.konami();
                    konamiIndex = 0;
                }
            } else if (key === 32 && state === 'paused') { // space
                HorseRace.play();
            } else if (key === 27) { // ESC
                HorseRace.pause();
            } else if (key === 37) {
                HorseRace.back();
            } else if (key === 39) {
                HorseRace.next();
            }
        });
    }

    HorseRace.konami = function() {
        ajax('konami-dance.json', function (json) {
            var data = json['dance'];
            TROT_STEPS = 1;
            data.forEach(function(a){a.forEach(function(h){
                h.key = h.key + 999999;
                h.name = 'dancer';
                h.img = 'Mirri.png';
            })});
            window.setTimeout(function doChunk() {
                processScores(data[0], 0.317);
                var waves = Array.prototype.slice.call(horserace.getElementsByClassName('wave'));
                waves.forEach(function(el){el.classList.add('konami');});
                data.splice(0, 1);
                if (data.length) {
                    window.setTimeout(doChunk, 400);
                } else {
                    window.setTimeout(HorseRace.play, 2000);
                    TROT_STEPS = 4;
                }
            });
        });
    };

    HorseRace.start = function(event) {
        getLastRound(event, function(l) { replay(event, l); });
    };

    HorseRace.clearSelection = function() {
        var waves = Array.prototype.slice.call(horserace.getElementsByClassName('wave'));
        waves.forEach(function(el){el.classList.remove('clicked');});
    };
}());

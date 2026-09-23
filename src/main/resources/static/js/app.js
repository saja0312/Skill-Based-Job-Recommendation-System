let currentUser = JSON.parse(localStorage.getItem("skillmatchUser") || "null");
let allJobs = [];
let remoteOnly = false;

function api(url, options={}) {
  return fetch(url, {headers: {"Content-Type":"application/json", ...(options.headers||{})}, ...options})
    .then(async r => {
      const data = await r.json().catch(()=>null);
      if(!r.ok) throw new Error(data?.message || data?.error || "Request failed");
      return data;
    });
}

function setActive(el){document.querySelectorAll(".nav-item").forEach(x=>x.classList.remove("active"));el?.classList.add("active")}
function showDashboard(el){
  setActive(el || document.querySelector(".nav-item"));
  document.getElementById("dashboardView").style.display="block";
  document.getElementById("contentView").style.display="none";
  loadDashboard();
}
function updateUserUI(){
  document.getElementById("auth").style.display=currentUser?"none":"block";
  document.getElementById("app").style.display=currentUser?"block":"none";
  if(currentUser){
    document.getElementById("greeting").textContent=`Hi, ${currentUser.name}`;
    document.getElementById("userRole").textContent=currentUser.role || "CANDIDATE";
    document.getElementById("avatar").textContent=(currentUser.name||"S").charAt(0).toUpperCase();
  }
}
async function login(){
  try{
    const data=await api("/api/auth/login",{method:"POST",body:JSON.stringify({
      email:document.getElementById("loginEmail").value,
      password:document.getElementById("loginPassword").value
    })});
    currentUser=data;localStorage.setItem("skillmatchUser",JSON.stringify(data));updateUserUI();showDashboard();
  }catch(e){alert(e.message)}
}
async function register(){
  try{
    const data=await api("/api/auth/register",{method:"POST",body:JSON.stringify({
      name:document.getElementById("regName").value,
      email:document.getElementById("regEmail").value,
      password:document.getElementById("regPassword").value,
      skills:document.getElementById("regSkills").value,
      experienceYears:0,preferredRole:"",location:"",remotePreferred:true
    })});
    alert("Account created successfully. Please login.");
    document.getElementById("loginEmail").value=data.email;
    document.getElementById("regName").value="";
    document.getElementById("regEmail").value="";
    document.getElementById("regPassword").value="";
    document.getElementById("regSkills").value="";
  }catch(e){alert(e.message)}
}
function logout(){localStorage.removeItem("skillmatchUser");currentUser=null;updateUserUI()}
async function loadDashboard(){
  if(!currentUser)return;
  document.getElementById("dashboardView").style.display="block";
  document.getElementById("contentView").style.display="none";
  try{
    const [jobs,recs,apps]=await Promise.all([
      api("/api/jobs"),
      api("/api/recommendations/"+currentUser.id),
      api("/api/applications/candidate/"+currentUser.id)
    ]);
    document.getElementById("jobCount").textContent=jobs.length;
    document.getElementById("topMatch").textContent=recs.length?recs[0].matchPercentage+"%":"—";
    document.getElementById("appCount").textContent=apps.length;
    document.getElementById("recommendations").innerHTML=recs.slice(0,6).map(recommendationCard).join("");
  }catch(e){document.getElementById("recommendations").innerHTML=`<div class="msg">${esc(e.message)}</div>`}
}
function recommendationCard(r){
  const j=r.job;
  const score=Math.max(0,Math.min(100,r.matchPercentage));
  const chips=(r.matchedSkills||[]).slice(0,4).map(s=>`<span class="chip matchchip">✓ ${esc(s)}</span>`).join("");
  const missing=(r.missingSkills||[]).slice(0,2).map(s=>`<span class="chip">${esc(s)}</span>`).join("");
  return `<article class="job-card">
    <div class="job-top"><div><h3>${esc(j.title)}</h3><div class="company">${esc(j.company||"Company")}</div></div>
    <div class="match" style="--score:${score}%"><span>${score}%</span></div></div>
    <p class="job-desc">${esc(j.description||"Great opportunity for a growing engineering team.")}</p>
    <div class="chips">${chips}${missing}</div>
    <div class="job-meta"><span>⌖ ${esc(j.location||"Anywhere")}</span><span>${j.remote?"◉ Remote":"On-site"}</span><span>◆ ${j.minExperience||0}+ yrs</span></div>
    <div class="job-actions"><button class="apply" onclick="applyJob(${j.id})">Apply now →</button><button class="details" onclick='showDetails(${JSON.stringify(j)},${score},${JSON.stringify(r.matchedSkills||[])},${JSON.stringify(r.missingSkills||[])})'>Details</button></div>
  </article>`;
}
async function loadRecommendations(){
  document.getElementById("dashboardView").style.display="none";
  document.getElementById("contentView").style.display="block";
  document.getElementById("pageTitle").textContent="Jobs picked for you";
  document.getElementById("pageSubtitle").textContent="Every result is scored against your profile.";
  try{
    const recs=await api("/api/recommendations/"+currentUser.id);
    document.getElementById("content").innerHTML=`<div class="jobs-grid">${recs.map(recommendationCard).join("")}</div>`;
  }catch(e){document.getElementById("content").innerHTML=`<div class="msg">${esc(e.message)}</div>`}
}
async function loadJobs(){
  document.getElementById("dashboardView").style.display="none";
  document.getElementById("contentView").style.display="block";
  document.getElementById("pageTitle").textContent="Explore all jobs";
  document.getElementById("pageSubtitle").textContent="Search 25+ roles across popular technology companies.";
  try{
    const q=(document.getElementById("search")?.value||"").trim();
    allJobs=await api("/api/jobs"+(q?`?q=${encodeURIComponent(q)}`:""));
    populateLocations();filterJobs();
  }catch(e){document.getElementById("content").innerHTML=`<div class="msg">${esc(e.message)}</div>`}
}
function populateLocations(){
  const select=document.getElementById("locationFilter"); if(!select)return;
  const current=select.value;
  const locations=[...new Set(allJobs.map(j=>j.location).filter(Boolean))].sort();
  select.innerHTML='<option value="">All locations</option>'+locations.map(x=>`<option>${esc(x)}</option>`).join("");
  select.value=current;
}
function filterJobs(){
  const loc=document.getElementById("locationFilter")?.value||"";
  const filtered=allJobs.filter(j=>(!loc||j.location===loc)&&(!remoteOnly||j.remote));
  document.getElementById("content").innerHTML=filtered.length
    ? `<div class="jobs-grid">${filtered.map(j=>simpleJobCard(j)).join("")}</div>`
    : '<div class="msg">No jobs match your filters.</div>';
}
function toggleRemote(){
  remoteOnly=!remoteOnly;
  document.getElementById("remoteFilter").classList.toggle("active",remoteOnly);
  filterJobs();
}
function simpleJobCard(j){
  return `<article class="job-card"><div class="job-top"><div><h3>${esc(j.title)}</h3><div class="company">${esc(j.company||"Company")}</div></div><div class="match" style="--score:100%"><span>JOB</span></div></div>
  <p class="job-desc">${esc(j.description||"")}</p><div class="chips">${(j.skills||"").split(",").slice(0,5).map(s=>`<span class="chip">${esc(s.trim())}</span>`).join("")}</div>
  <div class="job-meta"><span>⌖ ${esc(j.location||"Anywhere")}</span><span>${j.remote?"◉ Remote":"On-site"}</span><span>◆ ${j.minExperience||0}+ yrs</span></div>
  <div class="job-actions"><button class="apply" onclick="applyJob(${j.id})">Apply now →</button></div></article>`;
}
async function applyJob(id){
  try{await api(`/api/applications?candidateId=${currentUser.id}&jobId=${id}`,{method:"POST"});alert("Application submitted successfully.");loadDashboard();}
  catch(e){alert(e.message)}
}
async function loadApplications(){
  document.getElementById("dashboardView").style.display="none";
  document.getElementById("contentView").style.display="block";
  document.getElementById("pageTitle").textContent="My applications";
  document.getElementById("pageSubtitle").textContent="Track the jobs you've applied for.";
  try{
    const apps=await api("/api/applications/candidate/"+currentUser.id);
    document.getElementById("content").innerHTML=apps.length?`<div class="jobs-grid">${apps.map(a=>`<article class="job-card"><h3>${esc(a.job.title)}</h3><div class="company">${esc(a.job.company||"")}</div><div class="job-meta"><span>⌖ ${esc(a.job.location||"")}</span><span>Applied ${new Date(a.appliedAt).toLocaleDateString()}</span></div><span class="chip matchchip">${esc(a.status)}</span></article>`).join("")}</div>`:'<div class="msg">You have not applied to any jobs yet.</div>';
  }catch(e){alert(e.message)}
}
function showDetails(j,score,matched,missing){
  alert(`${j.title}\n\n${j.company||"Company"} · ${j.location||"Anywhere"}\nMatch: ${score}%\n\nMatched skills: ${matched.join(", ")||"None"}\nMissing skills: ${missing.join(", ")||"None"}\n\n${j.description||""}`);
}
function esc(v){return String(v??"").replace(/[&<>"']/g,c=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]))}
function init(){updateUserUI();if(currentUser)loadDashboard()}
init();
